package com.incodelabs.alignedexecutionengine.integration.verification.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String token;
    private String message;
    private boolean success;
}