package com.incodelabs.alignedexecutionengine.api.http;

import com.incodelabs.alignedexecutionengine.api.dto.CreatePolicyRequest;

import com.incodelabs.alignedexecutionengine.service.PolicyCheckService;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/policycheck")
@RequiredArgsConstructor
public class PolicyCheckController {
    private final PolicyCheckService policyCheckService;

    @PostMapping("/all/{actionId}")
    public List<Map<String, Object>> getPolicyChecksByActionID(@PathVariable String actionId) {
        return policyCheckService.getPolicyChecksByAction(actionId);
    }

    @GetMapping("/{policyId}")
    public Map<String, Object> getPolicyCheck(@PathVariable String policyId) {
        return policyCheckService.getPolicyCheck(policyId);
    }

    @GetMapping("/actionplan/{actionPlanId}")
    public List<Map<String, Object>> getPolicyChecksByActionPlan(@PathVariable String actionPlanId) {
        return policyCheckService.getPolicyChecksByActionPlan(actionPlanId);
    }
}
