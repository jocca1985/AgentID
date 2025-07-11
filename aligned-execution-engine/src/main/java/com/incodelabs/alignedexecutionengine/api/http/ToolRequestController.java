package com.incodelabs.alignedexecutionengine.api.http;

import com.incodelabs.alignedexecutionengine.service.ToolRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/toolrequest")
@RequiredArgsConstructor
public class ToolRequestController {
    private final ToolRequestService toolRequestService;

    @GetMapping("/{toolRequestId}")
    public Map<String, Object> getToolRequestById(@PathVariable String toolRequestId) {
        return toolRequestService.getToolRequest(toolRequestId);
    }

    @GetMapping("/actionplan/{actionPlanId}")
    public List<Map<String, Object>> getToolRequestsByActionPlanId(@PathVariable String actionPlanId) {
        return toolRequestService.getToolRequestsByActionPlan(actionPlanId);
    }
}
