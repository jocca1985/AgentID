package com.incodelabs.alignedexecutionengine.service;

import com.incodelabs.alignedexecutionengine.integration.verification.IncodeVerificationApiClient;
import com.incodelabs.alignedexecutionengine.integration.verification.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationService {
    
    private final IncodeVerificationApiClient verificationApiClient;
    
    /**
     * Start a new identity verification process
     */
    public StartVerificationResponse startVerification(String userEmail) {
        log.info("Starting verification process for user: {}", userEmail);
        return verificationApiClient.startVerification(userEmail);
    }
    
    /**
     * Check the status of a verification process
     */
    public VerificationStatusResponse checkVerificationStatus(String verificationId) {
        log.info("Checking verification status for ID: {}", verificationId);
        return verificationApiClient.checkVerificationStatus(verificationId);
    }
    
    /**
     * Get an authentication token for a user
     */
    public TokenResponse getToken(String userEmail) {
        log.info("Getting token for user: {}", userEmail);
        return verificationApiClient.getToken(userEmail);
    }
    
    /**
     * Validate an authentication token
     */
    public TokenValidationResponse validateToken(String token) {
        log.info("Validating token");
        return verificationApiClient.validateToken(token);
    }
    
    /**
     * Check if a token is valid and return user email if valid
     */
    public String getUserEmailFromToken(String token) {
        TokenValidationResponse validation = validateToken(token);
        if (validation.isSuccess() && validation.isValid()) {
            return validation.getUserEmail();
        }
        return null;
    }

    
    /**
     * Poll verification status until completion or timeout
     * Checks every 5 seconds for up to 4 minutes
     * Returns when status is SUCCESS or FAILED, or after timeout
     */
    public VerificationStatusResponse pollVerificationStatus(String verificationId) {
        log.info("Starting to poll verification status for ID: {}", verificationId);
        
        LocalDateTime startTime = LocalDateTime.now();
        Duration timeout = Duration.ofMinutes(4);
        Duration pollInterval = Duration.ofSeconds(5);
        
        while (Duration.between(startTime, LocalDateTime.now()).compareTo(timeout) < 0) {
            VerificationStatusResponse response = checkVerificationStatus(verificationId);
            
            if (response == null) {
                log.warn("Received null response while polling verification status for ID: {}", verificationId);
                sleep(pollInterval);
                continue;
            }
            
            String status = response.getStatus();
            log.debug("Polling verification ID: {}, current status: {}", verificationId, status);
            
            if ("SUCCESS".equalsIgnoreCase(status) || "FAILED".equalsIgnoreCase(status)) {
                log.info("Verification completed for ID: {} with status: {}", verificationId, status);
                return response;
            }
            
            if (!"PENDING".equalsIgnoreCase(status)) {
                log.warn("Unexpected verification status '{}' for ID: {}, continuing to poll", status, verificationId);
            }
            
            sleep(pollInterval);
        }
        
        log.warn("Verification polling timed out after {} minutes for ID: {}", timeout.toMinutes(), verificationId);
        
        // Return the last known status after timeout
        VerificationStatusResponse finalResponse = checkVerificationStatus(verificationId);
        if (finalResponse != null) {
            log.info("Final verification status after timeout for ID: {}: {}", verificationId, finalResponse.getStatus());
        }
        return finalResponse;
    }
    
    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Verification polling interrupted", e);
            throw new RuntimeException("Verification polling was interrupted", e);
        }
    }
}