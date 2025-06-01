package com.incodelabs.alignedexecutionengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class McpHealthCheckService {
    
    @Qualifier("mcpChatClient")
    private final ChatClient mcpChatClient;
    
    private final AtomicInteger consecutiveFailures = new AtomicInteger(0);
    private final AtomicLong totalHealthChecks = new AtomicLong(0);
    private final AtomicLong successfulHealthChecks = new AtomicLong(0);
    private volatile String lastHealthCheckTime = "Never";
    private volatile String lastSuccessTime = "Never";
    private volatile String connectionStatus = "UNKNOWN";
    
    @Scheduled(fixedRate = 28000) // Every 25 seconds
    public void performHealthCheck() {
        long checkNumber = totalHealthChecks.incrementAndGet();
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        lastHealthCheckTime = currentTime;
        
        log.info("Performing MCP health check #{}", checkNumber);
        
        try {
            // Use a simple prompt that should trigger MCP tool usage
            String response = mcpChatClient.prompt()
                    .user("Please check account balance for health check purposes")
                    .call()
                    .content();
            
            // Check if response indicates successful tool call
            if (response != null && !response.toLowerCase().contains("don't have the capability")) {
                // Health check passed
                successfulHealthChecks.incrementAndGet();
                consecutiveFailures.set(0);
                connectionStatus = "HEALTHY";
                lastSuccessTime = currentTime;
                
                log.info("MCP health check PASSED");
            } else {
                // Health check failed - tools not available
                handleHealthCheckFailure(checkNumber, "Tools not accessible: " + response);
            }
            
        } catch (Exception e) {
            // Health check failed - exception occurred
            handleHealthCheckFailure(checkNumber, "Exception: " + e.getMessage());
        }
        
        // Log summary every 10 checks
        if (checkNumber % 10 == 0) {
            logHealthSummary();
        }
    }
    
    private void handleHealthCheckFailure(long checkNumber, String reason) {
        int failures = consecutiveFailures.incrementAndGet();
        connectionStatus = "UNHEALTHY";
        
        log.warn("MCP health check #{} FAILED (consecutive failures: {}) - Reason: {}", 
                checkNumber, failures, reason);
        
        // Alert on critical failure thresholds
        if (failures == 3) {
            log.error("ALERT: 3 consecutive MCP health check failures detected! Connection may be unstable.");
        } else if (failures == 10) {
            log.error("CRITICAL: 10 consecutive MCP health check failures! Connection likely broken.");
        }
    }
    
    private void logHealthSummary() {
        long total = totalHealthChecks.get();
        long successful = successfulHealthChecks.get();
        double successRate = total > 0 ? (successful * 100.0 / total) : 0.0;
        
        log.info("MCP Health Summary - Total checks: {}, Successful: {}, Success rate: {:.1f}%, " +
                "Current status: {}, Last success: {}", 
                total, successful, successRate, connectionStatus, lastSuccessTime);
    }
    
    // Public methods for monitoring
    public String getConnectionStatus() {
        return connectionStatus;
    }
    
    public int getConsecutiveFailures() {
        return consecutiveFailures.get();
    }
    
    public double getSuccessRate() {
        long total = totalHealthChecks.get();
        long successful = successfulHealthChecks.get();
        return total > 0 ? (successful * 100.0 / total) : 0.0;
    }
    
    public String getHealthInfo() {
        return String.format("Status: %s | Success Rate: %.1f%% | Consecutive Failures: %d | Last Check: %s | Last Success: %s",
                connectionStatus, getSuccessRate(), consecutiveFailures.get(), lastHealthCheckTime, lastSuccessTime);
    }
}