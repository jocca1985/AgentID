package com.incodesmile.idvmcp.tool_dto;

public record TokenValidationResponse(
        String message,
        boolean success,
        boolean valid,
        String userEmail
) {
}
