package com.incodelabs.alignedexecutionengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.incodelabs.alignedexecutionengine.repository.PolicyCheckRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PolicyCheckService {

    private final PolicyCheckRepository policyCheckRepository;

    // Create a new policy check and return the policyId
    public String createPolicyCheck(String actionId) {
        String policyId = UUID.randomUUID().toString();
        log.info("Creating new policy check with ID: {} for action: {}", policyId, actionId);
        policyCheckRepository.createPolicyCheck(policyId, actionId);
        return policyId;
    }

    // Retrieve a policy check by policyId
    public Map<String, Object> getPolicyCheck(String policyId) {
        log.info("Retrieving policy check: {}", policyId);
        return policyCheckRepository.getPolicyCheck(policyId);
    }

    // Retrieve all policy checks for an action
    public List<Map<String, Object>> getPolicyChecksByAction(String actionId) {
        log.info("Retrieving all policy checks for action: {}", actionId);
        return policyCheckRepository.getPolicyChecksByAction(actionId);
    }

    // Retrieve all policy checks for an action plan
    public List<Map<String, Object>> getPolicyChecksByActionPlan(String actionPlanId) {
        log.info("Retrieving all policy checks for action plan: {}", actionPlanId);
        return policyCheckRepository.getPolicyChecksByActionPlan(actionPlanId);
    }

    // Complete a policy check with decision and policy triggered
    public void completePolicyCheck(String policyId, String status, String decision, String policyTriggered) {
        log.info("Completing policy check: {} with decision: {} and policy: {}", policyId, decision, policyTriggered);
        policyCheckRepository.completePolicyCheck(policyId, status, decision, policyTriggered);
    }

    // Update policy status
    public void updatePolicyStatus(String policyId, String status, String decision, String policyTriggered) {
        log.info("Updating policy status: {} to: {}", policyId, decision);
        policyCheckRepository.updatePolicyStatus(policyId, status, decision, policyTriggered);
    }

    // Get policy status
    public String getPolicyStatus(String policyId) {
        log.info("Getting policy status for: {}", policyId);
        return policyCheckRepository.getPolicyStatus(policyId);
    }

    // Delete a policy check by policyId
    public void deletePolicyCheck(String policyId) {
        log.info("Deleting policy check: {}", policyId);
        policyCheckRepository.deletePolicyCheck(policyId);
    }
}
