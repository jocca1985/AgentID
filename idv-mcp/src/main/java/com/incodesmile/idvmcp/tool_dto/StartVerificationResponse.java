package com.incodesmile.idvmcp.tool_dto;

public record StartVerificationResponse(
        String verificationTraceId,
        String verificationLink,
        String message,
        boolean success
) {
}
