package com.incodelabs.alignedexecutionengine.api.http;

// import com.incodelabs.alignedexecutionengine.integration.dto.ActionPlan;
import com.incodelabs.alignedexecutionengine.service.ActionPlanService;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/actionplan")
@RequiredArgsConstructor
public class ActionPlanController {
    private final ActionPlanService actionPlanService;

    @GetMapping("/session/{sessionId}")
    public List<Map<String, Object>> getActionPlansBySession(@PathVariable String sessionId) {
        return actionPlanService.getActionPlansBySession(sessionId);
    }

    @GetMapping("/{actionId}")
    public Map<String, Object> getActionPlan(@PathVariable String actionId) {
        return actionPlanService.getActionPlan(actionId);
    }
}
