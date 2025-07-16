package com.incodelabs.alignedexecutionengine.service;

import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import com.incodelabs.alignedexecutionengine.service.ActionPlanService;
import com.incodelabs.alignedexecutionengine.service.PolicyCheckService;
import com.incodelabs.alignedexecutionengine.service.ToolRequestService;
import com.incodelabs.alignedexecutionengine.service.FeedbackService;
import com.incodelabs.alignedexecutionengine.integration.dto.SessionContext;
import com.incodelabs.alignedexecutionengine.integration.dto.LoopContext;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;


@Service
@Slf4j
@RequiredArgsConstructor
public class ContextService {
    private final ActionPlanService actionPlanService;
    private final PolicyCheckService policyCheckService;
    private final ToolRequestService toolRequestService;
    private final QuerySessionService querySessionService;
    private final FeedbackService feedbackService;


    public SessionContext buildSessionContext(String sessionId) {
        log.info("Building context for session: {}", sessionId);
        
        String originalPrompt = (String) querySessionService.getQuerySession(sessionId).get("prompt");

        SessionContext context = SessionContext.builder()
            .sessionId(sessionId)
            .prompt(originalPrompt)
            .build();
        
        // Get all action plans for the session
        List<Map<String, Object>> actionPlans = actionPlanService.getActionPlansBySession(sessionId);
        context.setActionPlans(actionPlans);
        
        // Build loop history
        List<LoopContext> loops = new ArrayList<>();
        for (Map<String, Object> actionPlan : actionPlans) {

            Object actionIdObj = actionPlan.get("action_id");

            if (actionIdObj == null) {
                log.warn("Action plan missing action_id: {}", actionPlan);
                continue;
            }

            String actionId = actionIdObj.toString();
            
            LoopContext loop = buildLoopContext(actionId, actionPlan);
            loops.add(loop);
        }
        context.setLoops(loops);
        
        return context;
    }
    
    private LoopContext buildLoopContext(String actionId, Map<String, Object> actionPlan) {
        LoopContext loop = LoopContext.builder()
            .actionId(actionId)
            .actionPlan((String) actionPlan.get("plan"))
            .loopCount((Integer) actionPlan.get("loop_count"))
            .build();
        
        // Get policy checks for this action
        List<Map<String, Object>> policyChecks = policyCheckService.getPolicyChecksByAction(actionId);
        loop.setPolicyChecks(policyChecks);
        
        // Get tool requests for this action
        List<Map<String, Object>> toolRequests = toolRequestService.getToolRequestsByActionPlan(actionId);
        loop.setToolRequests(toolRequests);
        
        // Get feedback for this action
        List<Map<String, Object>> feedbackRequests = feedbackService.getFeedbackByAction(actionId);
        loop.setFeedbackRequests(feedbackRequests);
        
        return loop;
    }
    
    public String buildResumePrompt(String sessionId, String userFeedback) {
        SessionContext context = buildSessionContext(sessionId);

        String resumePrompt = "";
        resumePrompt += "Initial prompt: " + context.getPrompt() + "\n\n";
        
        // Add session history
        resumePrompt += "Session History:\n";
        resumePrompt += "Total loops: " + context.getLoops().size() + "\n\n";
        
        // Add each loop's context
        for (int i = 0; i < context.getLoops().size(); i++) {
            LoopContext loop = context.getLoops().get(i);
            resumePrompt += "--- Loop " + (i + 1) + " ---\n";
            resumePrompt += "Action Plan: " + loop.getActionPlan() + "\n";
            
            // Add policy decisions
            if (loop.getPolicyChecks() != null) {
                for (Map<String, Object> policyCheck : loop.getPolicyChecks()) {
                    String decision = (String) policyCheck.get("decision");
                    String policyTriggered = (String) policyCheck.get("policy_triggered");
                    resumePrompt += "Policy Decision: " + decision + " (" + policyTriggered + ")\n";
                }
            }
            
            // Add tool executions
            if (loop.getToolRequests() != null) {
                for (Map<String, Object> toolRequest : loop.getToolRequests()) {
                    String toolName = (String) toolRequest.get("tool_name");
                    String status = (String) toolRequest.get("status");
                    String responseData = (String) toolRequest.get("response_data");
                    resumePrompt += "Tool: " + toolName + " - " + status;
                    if (responseData != null && !responseData.isEmpty()) {
                        resumePrompt += " - " + responseData;
                    }
                    resumePrompt += "\n";
                }
            }
            
            // Add feedback requests
            if (loop.getFeedbackRequests() != null) {
                for (Map<String, Object> feedbackRequest : loop.getFeedbackRequests()) {
                    String feedback = (String) feedbackRequest.get("feedback");
                    String decision = (String) feedbackRequest.get("decision");
                    resumePrompt += "Feedback: " + feedback + " (" + decision + ")\n";
                }
            }
            resumePrompt += "\n";
        }
        
        resumePrompt += "User feedback for resume: " + userFeedback + "\n";
        
        return resumePrompt.toString();
    }
}