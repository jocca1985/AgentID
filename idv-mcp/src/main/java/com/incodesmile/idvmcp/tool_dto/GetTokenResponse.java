package com.incodesmile.idvmcp.tool_dto;

public record GetTokenResponse(
        String token,
        String message,
        boolean success
) {
}
