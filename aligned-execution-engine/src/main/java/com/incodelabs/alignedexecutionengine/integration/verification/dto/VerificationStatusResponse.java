package com.incodelabs.alignedexecutionengine.integration.verification.dto;

import lombok.Data;

@Data
public class VerificationStatusResponse {
    private String message;
    private boolean success;
    private String status;
    private String token;
    private String userEmail;
}