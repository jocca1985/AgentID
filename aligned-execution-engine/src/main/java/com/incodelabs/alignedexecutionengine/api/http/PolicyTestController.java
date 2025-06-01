package com.incodelabs.alignedexecutionengine.api.http;

import com.incodelabs.alignedexecutionengine.integration.PolicyApiClient;
import com.incodelabs.alignedexecutionengine.integration.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/policy")
@RequiredArgsConstructor
public class PolicyTestController {
    
    private final PolicyApiClient policyApiClient;
    
    @PostMapping("/create")
    public Map<String, Object> createPolicy(@RequestParam String id, 
                                           @RequestParam String text,
                                           @RequestParam(required = false) List<String> tags) {
        PolicyIn policy = PolicyIn.builder()
                .id(id)
                .text(text)
                .tags(tags != null ? tags : List.of())
                .build();
        
        Optional<PolicyResponse> response = policyApiClient.createOrReplacePolicy(policy);
        return Map.of(
            "success", response.isPresent(),
            "response", response.orElse(null)
        );
    }
    
    @DeleteMapping("/delete/{policyId}")
    public Map<String, Object> deletePolicy(@PathVariable String policyId) {
        boolean success = policyApiClient.deletePolicy(policyId);
        return Map.of("success", success);
    }
    
    @PostMapping("/check/prompt")
    public Map<String, Object> checkPrompt(@RequestParam String prompt,
                                          @RequestParam(required = false) String policyId) {
        CheckPromptIn.CheckPromptInBuilder builder = CheckPromptIn.builder()
                .prompt(prompt);
        
        if (policyId != null && !policyId.trim().isEmpty()) {
            builder.policyId(policyId);
        }
        
        CheckPromptIn checkRequest = builder.build();
        Optional<DecisionOut> decision = policyApiClient.checkPrompt(checkRequest);
        return Map.of(
            "success", decision.isPresent(),
            "decision", decision.orElse(null)
        );
    }

    
    @PostMapping("/check/prompt-all")
    public Map<String, Object> checkPromptAgainstAll(@RequestParam String prompt) {
        Optional<DecisionOut> decision = policyApiClient.checkPromptAgainstAllPolicies(prompt);
        return Map.of(
            "success", decision.isPresent(),
            "decision", decision.orElse(null)
        );
    }
    
    @PostMapping("/demo")
    public Map<String, Object> demoFlow() {
        // 1. Create a test policy
        PolicyIn testPolicy = PolicyIn.builder()
                .id("demo-policy")
                .text("Transfers above $10,000 require identity verification (IDV)")
                .tags(List.of("finance", "demo"))
                .build();
        
        Optional<PolicyResponse> createResponse = policyApiClient.createOrReplacePolicy(testPolicy);
        
        // 2. Test a prompt that should trigger the policy
        String testPrompt = "Transfer $20,000 to account 123456789";
        Optional<DecisionOut> promptDecision = policyApiClient.checkPromptAgainstAllPolicies(testPrompt);
        
        // 3. Test an output with actions
        List<Action> actions = List.of(Action.builder()
                .tool("bank.transfer")
                .parameters(Map.of("amount", 20000, "currency", "USD", "to", "123456789"))
                .build());
        
        Optional<DecisionOut> outputDecision = policyApiClient.checkOutputAgainstAllPolicies(
                "Sure, I'll transfer $20,000 now.", actions);
        
        return Map.of(
            "policyCreated", createResponse.isPresent(),
            "promptCheck", promptDecision.orElse(null),
            "outputCheck", outputDecision.orElse(null)
        );
    }
}