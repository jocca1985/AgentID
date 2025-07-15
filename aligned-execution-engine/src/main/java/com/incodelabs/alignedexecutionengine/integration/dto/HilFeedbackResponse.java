package com.incodelabs.alignedexecutionengine.integration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HilFeedbackResponse {
    private String summary;
    private String status;
    private HumanInputRequired humanInputRequired;
    private List<PlannedAction> plannedActions;
    private String nextSteps;
    private List<String> concerns;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class HumanInputRequired {
        private String type;
        private String description;
        private List<String> options;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PlannedAction {
        private int step;
        private String description;
        private String tool;
        private boolean requiresApproval;
        private String potentialImpact;
    }
}