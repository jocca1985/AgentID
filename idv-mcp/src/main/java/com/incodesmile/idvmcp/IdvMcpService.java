package com.incodesmile.idvmcp;

import com.incodesmile.idvmcp.tool_dto.CheckVerificationStatusResponse;
import com.incodesmile.idvmcp.tool_dto.GetTokenResponse;
import com.incodesmile.idvmcp.tool_dto.StartVerificationResponse;
import com.incodesmile.idvmcp.tool_dto.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdvMcpService {
    private final VerificationService verificationService;


    @Tool(name = "start-verification", description = "Starts identity verification process. User needs to provide valid email. After verification is completed token can be obtained.")
    public StartVerificationResponse start(@ToolParam(required = true, description = "User's email address for verification. To start verification process user needs to provide valid email.") String userEmail) {
        return verificationService.start(userEmail);
    }

    @Tool(name = "check-verification-status", description = "Checks the status of a verification for given verificationTraceId. verification trace ID needs to be provided. If status is success token will be created.")
    public CheckVerificationStatusResponse checkStatus(@ToolParam(required = true, description = "Verification trace ID to check verification status.") String verificationTraceId) {
        return verificationService.checkStatus(verificationTraceId);
    }

    @Tool(name = "validate-token", description = "Validates a JWT token. User needs to provide a JWT token.")
    public TokenValidationResponse validateToken(@ToolParam(description = "JWT token to validate") String token) {
        return verificationService.validateToken(token);
    }

    @Tool(name = "get-user-token", description = "Get user token by email. If token is not available, user needs to verify their identity first.")
    public GetTokenResponse getToken(@ToolParam(description = "User's email to get token for. Get a user token by email if available") String email) {
        return verificationService.getToken(email);
    }

}
