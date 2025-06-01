package com.mcpexample.demo;

public record StoreTokenToolResponse(
        boolean success,
        boolean needsVerification,
        String message
) {
}
