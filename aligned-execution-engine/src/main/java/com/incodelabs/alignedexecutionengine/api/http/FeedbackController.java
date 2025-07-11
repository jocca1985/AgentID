package com.incodelabs.alignedexecutionengine.api.http;

import com.incodelabs.alignedexecutionengine.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/{feedbackId}")
    public Map<String, Object> getFeedbackById(@PathVariable String feedbackId) {
        return feedbackService.getFeedbackRequest(feedbackId);
    }

    @GetMapping("/action/{actionId}")
    public List<Map<String, Object>> getFeedbackByActionId(@PathVariable String actionId) {
        return feedbackService.getFeedbackByAction(actionId);
    }

    @GetMapping("/actionplan/{actionPlanId}")
    public List<Map<String, Object>> getFeedbackByActionPlanId(@PathVariable String actionPlanId) {
        return feedbackService.getFeedbackByActionPlan(actionPlanId);
    }
}
