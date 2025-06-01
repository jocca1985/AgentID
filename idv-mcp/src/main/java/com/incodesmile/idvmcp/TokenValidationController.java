package com.incodesmile.idvmcp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenValidationController {
    private final JwtTokenUtil jwtTokenUtil;

    public record TokenRequest(String token) {}
    public record TokenResponse(
            boolean valid,
            String userId,
            String message,
            String error,
            boolean needsVerification
    ) {}

    @PostMapping("/validate-token")
    public ResponseEntity<TokenResponse> validateToken(@RequestBody TokenRequest request) {
        String token = request.token();

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TokenResponse(false, null, "Token is required", null, true));
        }

        var validation = jwtTokenUtil.validateToken(token);

        if (validation.valid()) {
            return ResponseEntity.ok(
                    new TokenResponse(true, validation.decoded().getSubject(), "Token is valid", null, false)
            );
        } else {
            return ResponseEntity.ok(
                    new TokenResponse(false, null, "Token is invalid or expired", validation.error(), true)
            );
        }
    }

}
