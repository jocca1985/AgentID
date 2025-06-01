package com.incodesmile.idvmcp.tool_dto;

public record CheckVerificationStatusResponse(
        String message,
        boolean success,
        String status,
        String token,
        String userEmail
) {
}
