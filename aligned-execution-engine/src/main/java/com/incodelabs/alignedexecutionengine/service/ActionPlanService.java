package com.incodelabs.alignedexecutionengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.incodelabs.alignedexecutionengine.repository.ActionPlanRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActionPlanService {

    private final ActionPlanRepository actionPlanRepository;

    // Create a new action plan and return the actionId
    public String createActionPlan(String sessionId, int loopCount) {
        String actionId = UUID.randomUUID().toString();
        actionPlanRepository.createActionPlan(actionId, sessionId, loopCount);
        return actionId;
    }

    // Retrieve an action plan by actionId
    public Map<String, Object> getActionPlan(String actionId) {
        return actionPlanRepository.getActionPlan(actionId);
    }

    // Retrieve all action plans for a session
    public List<Map<String, Object>> getActionPlansBySession(String sessionId) {
        return actionPlanRepository.getActionPlansBySession(sessionId);
    }

    // Update an action plan (e.g., set plan or end_time)
    public void updateActionPlan(String actionId, String plan) {
        actionPlanRepository.updateActionPlan(actionId, plan);
    }

    // Delete an action plan by actionId
    public void deleteActionPlan(String actionId) {
        actionPlanRepository.deleteActionPlan(actionId);
    }
}
