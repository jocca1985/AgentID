package com.incodelabs.alignedexecutionengine.integration.verification.dto;

import lombok.Data;

@Data
public class TokenValidationResponse {
    private String message;
    private boolean success;
    private boolean valid;
    private String userEmail;
}