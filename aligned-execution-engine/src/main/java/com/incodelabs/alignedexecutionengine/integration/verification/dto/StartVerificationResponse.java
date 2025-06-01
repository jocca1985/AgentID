package com.incodelabs.alignedexecutionengine.integration.verification.dto;

import lombok.Data;

@Data
public class StartVerificationResponse {
    private String verificationTraceId;
    private String verificationLink;
    private String message;
    private boolean success;
}