package com.incodelabs.alignedexecutionengine.integration.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoopContext {
    private String sessionId;
    private String actionId;
    private String actionPlan;
    private Integer loopCount;
    private List<Map<String, Object>> policyChecks;
    private List<Map<String, Object>> toolRequests;
    private List<Map<String, Object>> feedbackRequests;
}
