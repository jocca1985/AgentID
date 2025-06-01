package com.incodelabs.alignedexecutionengine.service;

import com.incodelabs.alignedexecutionengine.integration.PolicyApiClient;
import com.incodelabs.alignedexecutionengine.integration.dto.*;
import com.incodelabs.alignedexecutionengine.integration.email.EmailClientApi;
import com.incodelabs.alignedexecutionengine.integration.email.dto.EmailRequest;
import com.incodelabs.alignedexecutionengine.integration.verification.dto.TokenResponse;
import com.incodelabs.alignedexecutionengine.integration.verification.dto.TokenValidationResponse;
import com.incodelabs.alignedexecutionengine.integration.verification.dto.VerificationStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.runtime.Token;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActionControllerService {
    private final PromptsUtil promptsUtil;
    private final ChatClient openAiChatClient;
    private final PolicyApiClient policyApi;
    private final McpClientService mcpClientService;
    private final VerificationService verificationService;
    private final EmailClientApi emailClientApi;
    
    private static final int MAX_FEEDBACK_LOOPS = 50;
    private String currentVerificationToken; // Store token for use in tool parameters

    public ActionFeedbackResponse testControllerAgent(String prompt) {
        ActionFeedbackResponse feedback = ActionFeedbackResponse.builder().build();
        
        try {
            // Step 1: Validate initial prompt
            Optional<DecisionOut> promptValidation = validateClientPrompt(prompt);
            
            if (promptValidation.isEmpty()) {
                log.warn("Prompt validation failed: {}", prompt);
                feedback.setErrorMessage("Prompt validation failed");
                return feedback;
            }
            
            DecisionOut promptDecision = promptValidation.get();
            feedback.setInputPromptFeedback(promptDecision);
            
            if (PerPolicy.AlignmentType.deny.equals(promptDecision.getAlignment())) {
                feedback.setCompleted(true);
                return feedback;
            }
            
            // Handle IDV case
            String processedPrompt = prompt;
            if (PerPolicy.AlignmentType.idv.equals(promptDecision.getAlignment())) {
                feedback.getExecutionSteps().add(Action.builder().tool("idv").build());
                
                try {
                    // Complete IDV process
                    String verificationToken = completeIdvProcess(feedback);
                    
                    if (verificationToken != null) {
                        this.currentVerificationToken = verificationToken;
                        processedPrompt = "Identity verification completed successfully with token available. Now we can proceed with " + prompt;
                    } else {
                        feedback.setErrorMessage("Identity verification failed");
                        return feedback;
                    }
                } catch (Exception e) {
                    log.error("IDV process failed", e);
                    feedback.setErrorMessage("Identity verification process failed: " + e.getMessage());
                    return feedback;
                }
            }

            feedback.setExecutionSteps(new ArrayList<>(List.of(Action.builder().tool("create-plan").build())));
            // Step 2: Start feedback loop if prompt is allowed.
            return executeFeedbackLoop(processedPrompt, feedback);
            
        } catch (Exception e) {
            log.error("Error in controller agent execution", e);
            feedback.setErrorMessage("Execution error: " + e.getMessage());
            return feedback;
        }
    }
    
    private ActionFeedbackResponse executeFeedbackLoop(String prompt, ActionFeedbackResponse feedback) {
        String currentPrompt = prompt;
        
        // Continue while there are execution steps remaining
        for (int iteration = 1; iteration <= MAX_FEEDBACK_LOOPS && !feedback.getExecutionSteps().isEmpty(); iteration++) {
            feedback.setLoopIteration(iteration);
            
            // Get next execution step
            Action currentStep = feedback.getExecutionSteps().removeFirst();
            log.info("Executing step: {}", currentStep);

            
            // For other steps, prepare action plan
            CheckOutputIn actionPlan = feedback.getActionPlan() == null ? prepareActionPlan(currentPrompt) : feedback.getActionPlan();
            feedback.setActionPlan(actionPlan);

            
            if (actionPlan == null || actionPlan.getLlmOutput() == null) {
                feedback.setErrorMessage("Failed to prepare action plan");
                return feedback;
            }

            feedback.setExecutionSteps(new ArrayList<>(actionPlan.getActions()));
            
            // Validate action plan with policy
            CheckOutputRequest policyRequest = CheckOutputRequest.builder()
                    .llmOutput(actionPlan.getLlmOutput())
                    .actions(actionPlan.getActions() != null ? actionPlan.getActions() : Collections.emptyList())
                    .build();
            
            feedback.setCheckOutputRequest(policyRequest);
            
            Optional<DecisionOut> policyDecision = policyApi.checkOutput(policyRequest);
            if (policyDecision.isEmpty()) {
                feedback.setErrorMessage("Policy validation failed");
                return feedback;
            }
            
            DecisionOut outputDecision = policyDecision.get();
            feedback.setOutputFeedback(outputDecision);
            
            if (PerPolicy.AlignmentType.deny.equals(outputDecision.getAlignment())) {
                feedback.setCompleted(true);
                return feedback;
            }
            TokenValidationResponse tokenValidation = new TokenValidationResponse();
            if (PerPolicy.AlignmentType.idv.equals(outputDecision.getAlignment())) {
                tokenValidation.setValid(false);
                tokenValidation.setMessage("null token");
                tokenValidation.setSuccess(false);
                if (currentVerificationToken != null) {
                    tokenValidation = verificationService.validateToken(currentVerificationToken);
                }
                if (!tokenValidation.isValid()) {
                    currentVerificationToken =  completeIdvProcess(feedback);
                }

                if (currentVerificationToken == null || currentVerificationToken.isEmpty()) {
                    feedback.setErrorMessage("IDV process failed, no token available");
                    return feedback;
                }
                outputDecision.setAlignment(PerPolicy.AlignmentType.allow);
            }
            
            if (PerPolicy.AlignmentType.allow.equals(outputDecision.getAlignment())) {
                // Execute tools if available
                if (!CollectionUtils.isEmpty(actionPlan.getActions())) {
                    for (Action action : actionPlan.getActions()) {
                        String toolResult = executeToolAction(action, feedback);
                        feedback.getToolExecutionResults().put(action.getTool(), toolResult);
                        feedback.setExecutionSteps(actionPlan.getActions().stream().filter(a -> !a.getTool().equals(action.getTool())).collect(Collectors.toList()));
                        // Add any new execution steps based on tool results if needed
                    }
                    addNewExecutionStepsIfNeeded(feedback);
                } else {
                    // No actions to execute for this step
                    log.info("No actions to execute for step: {}", currentStep);
                }
            }
        }
        
        // Mark as completed when all execution steps are done
        feedback.setCompleted(true);
        return feedback;
    }
    
    private String executeToolAction(Action action, ActionFeedbackResponse feedback) {
        String toolInstruction = formatToolInstruction(action);
        feedback.getToolExecutions().add("Executing: " + toolInstruction);
        
        String result = mcpClientService.executeTool(toolInstruction);
        feedback.getToolExecutions().add("Result: " + result);
        
        if (feedback.getFinalResult() == null) {
            feedback.setFinalResult(result);
        } else {
            feedback.setFinalResult(feedback.getFinalResult() + "; " + result);
        }
        
        return result;
    }
    
    private String formatToolInstruction(Action action) {
        StringBuilder instruction = new StringBuilder();
        instruction.append("Execute tool: ").append(action.getTool());
        
        // Combine original parameters with verification token if available
        Map<String, Object> allParameters = new HashMap<>();
        if (action.getParameters() != null) {
            allParameters.putAll(action.getParameters());
        }
        
        // Add verification token to parameters if available
        if (currentVerificationToken != null) {
            allParameters.put("token", currentVerificationToken);
            allParameters.put("auth_token", currentVerificationToken);
        }
        
        if (!allParameters.isEmpty()) {
            instruction.append(" with parameters: ");
            allParameters.forEach((key, value) -> 
                instruction.append(key).append("=").append(value).append(" "));
        }
        
        return instruction.toString().trim();
    }
    
    private void addNewExecutionStepsIfNeeded(ActionFeedbackResponse feedback) {
        String result = feedback.getToolExecutionResults().entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
        String previousPlannedActions = feedback.getActionPlan().getActions().stream().map(Action::getTool).collect(Collectors.joining(", "));
        String previousActionPlanInDetail = feedback.getActionPlan().getLlmOutput();
        String userPrompt = "Previous plan: {" + previousActionPlanInDetail + "}. Planned actions: {" + previousPlannedActions + "}. Result of executed actions: {" + result + "}. Should there be any new actions planned based on this result? If yes, please provide a new plan.";
        CheckOutputIn newPlan = openAiChatClient.prompt()
                .system(promptsUtil.newPlanPrompt())
                .user(userPrompt)
                .call()
                .entity(CheckOutputIn.class);
        if (newPlan != null && newPlan.getActions() != null && !newPlan.getActions().isEmpty()) {
            log.info("Adding new execution steps based on tool results: {}", newPlan.getActions());
            feedback.getExecutionSteps().addAll(newPlan.getActions());
            feedback.setActionPlan(newPlan);
        } else {
            log.info("No new actions planned based on tool results.");
            feedback.setExecutionSteps(new ArrayList<>());
        }
    }

    private CheckOutputIn prepareActionPlan(String prompt) {
        return openAiChatClient.prompt()
                .system(promptsUtil.actionPlanSystemPrompt())
                .user(prompt)
                .call()
                .entity(CheckOutputIn.class);
    }

    private Optional<DecisionOut> validateClientPrompt(String prompt) {
        return policyApi.checkPrompt(CheckPromptIn.builder().prompt(prompt).build());
    }
    
    /**
     * Complete the 3-step IDV process:
     * 1. Start verification process
     * 2. Poll by trace ID until status is SUCCESS or FAILED
     * 3. Get token for the email that started verification
     */
    private String completeIdvProcess(ActionFeedbackResponse feedback) {
        // Use a default email for IDV process - in real implementation this would come from the user context
        String userEmail = "ognjen.samardzic@incode.com";
        
        try {
            // Step 1: Start verification process
            log.info("Starting verification for user: {}", userEmail);
            var startResp = verificationService.startVerification(userEmail);
            var traceId = startResp.getVerificationTraceId();
            
            if (traceId == null) {
                log.error("Failed to start verification process for user: {}", userEmail);
                return null;
            }
            log.info("STARTED verification trace ID: {}", traceId);
            
            // Send verification link email using the link from startResp
            try {
                EmailRequest emailRequest = EmailRequest.builder()
                    .toEmail("osamardzic@gmail.com")
                    .fromEmail("jocca1985@gmail.com")
                    .subject("Verification link")
                    .content("Here is the link to verification: " + startResp.getVerificationLink())
                    .build();
                
                var emailResponse = emailClientApi.sendEmail(emailRequest);
                if (emailResponse.isSuccess()) {
                    log.info("Verification email sent successfully to: {}", userEmail);
                } else {
                    log.warn("Failed to send verification email: {}", emailResponse.getMessage());
                }
            } catch (Exception e) {
                log.error("Error sending verification email to: {}", userEmail, e);
            }

            
            log.info("Verification started with trace ID: {}", traceId);
            
            // Step 2: Poll verification status until completion
            log.info("Polling verification status for trace ID: {}", traceId);
            VerificationStatusResponse statusResponse = verificationService.pollVerificationStatus(traceId);
            
            if (statusResponse == null) {
                log.error("Failed to get verification status for trace ID: {}", traceId);
                return null;
            }
            
            String status = statusResponse.getStatus();
            log.info("Final verification status: {}", status);
            
            if (!"SUCCESS".equalsIgnoreCase(status)) {
                log.warn("Verification failed or timed out with status: {}", status);
                return null;
            }
            
            // Step 3: Get token for the verified user
            log.info("Getting authentication token for user: {}", userEmail);
            TokenResponse tokenResponse = verificationService.getToken(userEmail);
            
            if (tokenResponse == null || !tokenResponse.isSuccess() || tokenResponse.getToken() == null) {
                log.error("Failed to get token for user: {}", userEmail);
                return null;
            }
            
            log.info("IDV process completed successfully for user: {}", userEmail);
            return tokenResponse.getToken();
            
        } catch (Exception e) {
            log.error("Error during IDV process for user: {}", userEmail, e);
            throw e;
        }
    }
}
