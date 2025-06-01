package com.incodelabs.alignedexecutionengine.integration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerPolicy {
    @JsonProperty("policy_id")
    private String policyId;
    private AlignmentType alignment;
    private String reason;
    
    public enum AlignmentType {
        allow, deny, idv
    }
}