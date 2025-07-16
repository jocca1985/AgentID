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
public class SessionContext {
    private String sessionId;
    private String prompt;
    private List<LoopContext> loops;
    private List<Map<String, Object>> actionPlans;
}
