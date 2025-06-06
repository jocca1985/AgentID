package com.incodelabs.alignedexecutionengine.integration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckOutputRequest {
    @JsonProperty("policy_id")
    private String policyId;
    @JsonProperty("llm_output")
    private String llmOutput;

    @Builder.Default
    private List<Action> actions = Collections.emptyList();
}
