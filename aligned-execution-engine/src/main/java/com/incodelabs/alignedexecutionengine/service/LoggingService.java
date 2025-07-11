//package com.incodelabs.alignedexecutionengine.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import lombok.extern.slf4j.Slf4j;
//import com.incodelabs.alignedexecutionengine.repository.LoggingRepository;
//import com.incodelabs.alignedexecutionengine.integration.dto.LogEntry;
//
//@Slf4j
//@Service
//public class LoggingService {
//
//    @Autowired
//    private LoggingRepository loggingRepository;
//
//    // Generic logging method
//    public void log(LogEntry logEntry) {
//        loggingRepository.save(logEntry);
//    }
//
//    // Logging Methods
//
//    // Step 1: Query Session Lifecycle
//    public String startQuerySession(String prompt, String sessionId) {
//        return loggingRepository.createQuerySession(prompt, sessionId);
//    }
//
//    public void updateSessionPolicy(String sessionId, String policyDecision, String policyTriggered) {
//        loggingRepository.updateQuerySessionPolicyInfo(sessionId, policyDecision, policyTriggered);
//    }
//
//    // todo actioncontroller service
//    public void endQuerySession(String sessionId) {
//        loggingRepository.completeQuerySession(sessionId);
//    }
//
//    // Step 2: Action Lifecycle
//    public String startAction(String sessionId, Integer loopCount, String actionId) {
//        return loggingRepository.startAction(sessionId, loopCount, actionId);
//    }
//
//    public void endAction(String actionId, String plan) {
//        loggingRepository.completeAction(actionId, plan);
//    }
//
//    // Step 3: Policy Check Lifecycle
//    public String startPolicyCheck(String actionId, String policyId) {
//        return loggingRepository.startPolicyCheck(actionId, policyId);
//    }
//
//    public void endPolicyCheck(String policyId, String decision, String policyTriggered) {
//        loggingRepository.completePolicyCheck(policyId, decision, policyTriggered);
//    }
//
//    // Step 4: Tool Execution Lifecycle
//    public String createToolRequest(String actionId, String toolName, String toolRequestId) {
//        return loggingRepository.createToolRequest(actionId, toolName, toolRequestId);
//    }
//
//    public String startToolExecution(String actionId, String toolName) {
//        return loggingRepository.initiateToolExecution(actionId, toolName);
//    }
//
//    public void endToolExecution(String toolRequestId, String finalStatus) {
//        loggingRepository.completeToolExecution(toolRequestId, finalStatus);
//    }
//
//    // Step 5: Feedback Lifecycle
//    public void startFeedback(String actionId) {
//        loggingRepository.startFeedbackRequest(actionId);
//    }
//
//    public void endFeedback(String actionId, String feedback, String decision) {
//        loggingRepository.completeFeedbackRequest(actionId, feedback, decision);
//    }
//
//    // Utility methods
//    public void updateToolStatus(String toolRequestId, String status) {
//        loggingRepository.updateToolStatus(toolRequestId, status);
//    }
//}