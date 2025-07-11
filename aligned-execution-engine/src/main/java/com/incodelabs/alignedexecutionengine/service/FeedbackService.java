package com.incodelabs.alignedexecutionengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.incodelabs.alignedexecutionengine.repository.FeedbackRepository;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // Create a new feedback request
    public void createFeedbackRequest(String actionId) {
        log.info("Creating new feedback request for action: {}", actionId);
        feedbackRepository.createFeedbackRequest(actionId);
    }

    // Retrieve a feedback request by actionId
    public Map<String, Object> getFeedbackRequest(String actionId) {
        log.info("Retrieving feedback request for action: {}", actionId);
        return feedbackRepository.getFeedbackRequest(actionId);
    }

    // Retrieve all feedback requests
    public List<Map<String, Object>> getAllFeedbackRequests() {
        log.info("Retrieving all feedback requests");
        return feedbackRepository.getAllFeedbackRequests();
    }

    // Retrieve all feedback requests by actionId
    public List<Map<String, Object>> getFeedbackByAction(String actionId) {
        log.info("Retrieving feedback requests for action: {}", actionId);
        return feedbackRepository.getFeedbackByAction(actionId);
    }

    // Retrieve all feedback requests by actionPlanId
    public List<Map<String, Object>> getFeedbackByActionPlan(String actionPlanId) {
        log.info("Retrieving feedback requests for action plan: {}", actionPlanId);
        return feedbackRepository.getFeedbackByActionPlan(actionPlanId);
    }

    // Complete a feedback request with feedback and decision
    public void completeFeedbackRequest(String actionId, String feedback, String decision) {
        log.info("Completing feedback request for action: {} with decision: {}", actionId, decision);
        feedbackRepository.completeFeedbackRequest(actionId, feedback, decision);
    }

    // Update feedback content
    public void updateFeedback(String actionId, String feedback) {
        log.info("Updating feedback content for action: {}", actionId);
        feedbackRepository.updateFeedback(actionId, feedback);
    }
}
