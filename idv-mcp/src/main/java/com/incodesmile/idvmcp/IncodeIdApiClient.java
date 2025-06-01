package com.incodesmile.idvmcp;

import com.incodesmile.idvmcp.dto.WorkforceVerificationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class IncodeIdApiClient {
    private static final Logger logger = LoggerFactory.getLogger(IncodeIdApiClient.class);

    private static final long TOKEN_EXPIRY_SECONDS = 600; // 10 minutes in seconds
    private static final String SERVER_TOKEN_KEY = "serverToken";
    private static final String REDIRECT_URL = "https://incode-approve-demo.ngrok.app";


    @Value("${incode.base.url}")
    private String incodeBaseUrl;

    @Value("${incode.integration.id}")
    private String incodeIntegrationId;

    @Value("${incode.secret}")
    private String incodeSecret;

    @Value("${incode.api.key}")
    private String incodeApiKey;

    private final RestTemplate restTemplate;

    private final Map<String, CachedToken> tokenCache = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, VerificationInfo> pendingVerifications = new ConcurrentHashMap<>();


    private static class CachedToken {

        private final String token;
        private final Instant expiry;

        public CachedToken(String token, Instant expiry) {
            this.token = token;
            this.expiry = expiry;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expiry);
        }

        public String getToken() {return token;}

    }

    public record ServerTokenResponse(String token) {}

    public String getServerToken() {
        // Check if we have a valid cached token
        CachedToken cachedToken = tokenCache.get(SERVER_TOKEN_KEY);
        if (cachedToken != null && !cachedToken.isExpired()) {
            logger.debug("Using cached server token");
            return cachedToken.getToken();
        }

        logger.info("Getting server token from incode-id-service...");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", incodeApiKey);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("integrationId", incodeIntegrationId);
            requestBody.put("secret", incodeSecret);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<ServerTokenResponse> response = restTemplate.postForEntity(
                    incodeBaseUrl + "/v1/integration/authorize/server",
                    entity,
                    ServerTokenResponse.class
            );

            logger.info("Server token obtained successfully");
            String token = response.getBody().token();

            tokenCache.put(SERVER_TOKEN_KEY, new CachedToken(
                    token,
                    Instant.now().plusSeconds(TOKEN_EXPIRY_SECONDS)
            ));

            return token;
        } catch (Exception error) {
            logger.error("Error getting server token: {}", error.getMessage());
            throw error;
        }
    }

    public record VerificationLinkResponse(String verificationLink, String verificationTraceId, String correlationId) {}

    public record VerificationInfo(
            String correlationId,
            String userEmail,
            String status,
            String serverToken,
            String createdAt
    ) {}

    public VerificationLinkResponse generateVerificationLink(String serverToken, String userEmail) {
        logger.info("Generating verification link for {}...", userEmail);
        try {
            long now = Instant.now().toEpochMilli();
            String correlationId = "Agent_verification_" + userEmail + "_" + now;

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-auth-token", serverToken);

            // Set up request body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("integrationId", incodeIntegrationId);
            requestBody.put("loginHint", userEmail);
            requestBody.put("redirectUrl", REDIRECT_URL);
            requestBody.put("correlationId", correlationId);

            // Create the request entity with headers and body
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            // Define a response type class for parsing
            record GenerateVerificationResponse(String verificationLink, String verificationTraceId) {}

            // Make the POST request
            ResponseEntity<GenerateVerificationResponse> response = restTemplate.postForEntity(
                    incodeBaseUrl + "/v1/verification/generate-verification-link",
                    entity,
                    GenerateVerificationResponse.class
            );

            String verificationLink = response.getBody().verificationLink();
            String verificationTraceId = response.getBody().verificationTraceId();

            // Store verification information
            pendingVerifications.put(verificationTraceId, new VerificationInfo(
                    correlationId,
                    userEmail,
                    "PENDING",
                    serverToken,
                    Instant.now().toString()
            ));

            return new VerificationLinkResponse(verificationLink, verificationTraceId, correlationId);
        } catch (Exception error) {
            logger.error("Error generating verification link: {}", error.getMessage());
            throw error;
        }
    }

    public WorkforceVerificationResponse checkVerificationStatus(String verificationTraceId) {
        try {
            logger.info("Calling {}/v1/workforce/verification/{} endpoint...", incodeBaseUrl, verificationTraceId);

            // Get a fresh server token
            String serverToken = getServerToken();

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-auth-token", serverToken);

            // Create the request entity with headers
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // Make the GET request
            ResponseEntity<WorkforceVerificationResponse> response = restTemplate.exchange(
                    incodeBaseUrl + "/v1/workforce/verification/" + verificationTraceId,
                    HttpMethod.GET,
                    entity,
                    WorkforceVerificationResponse.class
            );

            logger.info("Verification status obtained successfully");
            return response.getBody();
        } catch (Exception error) {
            logger.error("Error checking verification status: {}", error.getMessage());
            throw error;
        }
    }
}

