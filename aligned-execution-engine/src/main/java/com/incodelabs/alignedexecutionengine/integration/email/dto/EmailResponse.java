package com.incodelabs.alignedexecutionengine.integration.email.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailResponse {
    private boolean success;
    private String message;
    private String messageId;
}