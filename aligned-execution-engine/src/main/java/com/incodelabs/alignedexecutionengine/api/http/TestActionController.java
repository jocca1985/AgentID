package com.incodelabs.alignedexecutionengine.api.http;

import com.incodelabs.alignedexecutionengine.service.ActionControllerService;
import com.incodelabs.alignedexecutionengine.service.AsyncProcessingService;
import com.incodelabs.alignedexecutionengine.service.McpClientService;
import com.incodelabs.alignedexecutionengine.service.McpHealthCheckService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestActionController {
    private final AsyncProcessingService asyncProcessingService;
    private final McpClientService mcpClientService;
    private final McpHealthCheckService mcpHealthCheckService;

    @GetMapping("/test-controller-agent")
    public Map<String, String> testControllerAgent(@RequestParam String prompt) {
        String sessionId = asyncProcessingService.startAsyncProcessing(prompt);
        return Map.of(
            "message", "Processing...",
            "sessionId", sessionId,
            "status", "PROCESSING"
        );
    }

    // post mapping for vapi voice agent where prompt is in the body
    @PostMapping("/test-controller-agent")
    public Map<String, String> testControllerAgent(@RequestBody HashMap<String, String> body) {
        String prompt = body.get("prompt");
        String sessionId = asyncProcessingService.startAsyncProcessing(prompt);
        return Map.of(
                "message", "Processing...",
                "sessionId", sessionId,
                "status", "PROCESSING"
        );
    }
    
    @GetMapping("/get-results/{sessionId}")
    public ResponseEntity<?> getResults(@PathVariable String sessionId) {
        AsyncProcessingService.ProcessingSession session = asyncProcessingService.getSession(sessionId);
        
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        
        switch (session.getStatus()) {
            case PROCESSING:
                return ResponseEntity.ok(Map.of(
                    "sessionId", sessionId,
                    "status", "PROCESSING",
                    "message", "Still processing...",
                    "elapsedTimeMs", System.currentTimeMillis() - session.getStartTime()
                ));
                
            case COMPLETED:
                return ResponseEntity.ok(Map.of(
                    "sessionId", sessionId,
                    "status", "COMPLETED",
                    "result", session.getResult(),
                    "elapsedTimeMs", System.currentTimeMillis() - session.getStartTime()
                ));
                
            case FAILED:
                return ResponseEntity.ok(Map.of(
                    "sessionId", sessionId,
                    "status", "FAILED",
                    "error", session.getErrorMessage(),
                    "elapsedTimeMs", System.currentTimeMillis() - session.getStartTime()
                ));
                
            default:
                return ResponseEntity.internalServerError().build();
        }
    }

    // Post mapping for vapi voice agent where session id is in the body
    @PostMapping("/get-results")
    public ResponseEntity<?> getResultsPost(@RequestBody HashMap<String, String> body) {
        String sessionId = body.get("session_id");
        
        if (sessionId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "session_id is required"));
        }
        
        AsyncProcessingService.ProcessingSession session = asyncProcessingService.getSession(sessionId);
        
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        
        switch (session.getStatus()) {
            case PROCESSING:
                return ResponseEntity.ok(Map.of(
                    "sessionId", sessionId,
                    "status", "PROCESSING",
                    "message", "Still processing...",
                    "elapsedTimeMs", System.currentTimeMillis() - session.getStartTime()
                ));
                
            case COMPLETED:
                return ResponseEntity.ok(Map.of(
                    "sessionId", sessionId,
                    "status", "COMPLETED",
                    "result", session.getResult(),
                    "elapsedTimeMs", System.currentTimeMillis() - session.getStartTime()
                ));
                
            case FAILED:
                return ResponseEntity.ok(Map.of(
                    "sessionId", sessionId,
                    "status", "FAILED",
                    "error", session.getErrorMessage(),
                    "elapsedTimeMs", System.currentTimeMillis() - session.getStartTime()
                ));
                
            default:
                return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/resume-processing/{sessionId}")
    public Map<String, String> testHilFeedback(@PathVariable String sessionId, @RequestBody String feedback) {
        // calls on asyncprocessing service to resume processing, however with a resume flag set to true
        log.info("Resuming processing for session: {}", sessionId);
        asyncProcessingService.resumeAsyncProcessing(sessionId, feedback);
        return Map.of(
            "message", "Resuming processing...",
            "sessionId", sessionId,
            "status", "PROCESSING"
        );
    }

    @GetMapping("/test-mcp-client")
    public Map<String, String> testMcpClient() {
        String response = mcpClientService.checkBalance();
        return Map.of("response", response);
    }

    @GetMapping("/mcp-health")
    public Map<String, Object> getMcpHealth() {
        return Map.of(
            "status", mcpHealthCheckService.getConnectionStatus(),
            "successRate", mcpHealthCheckService.getSuccessRate(),
            "consecutiveFailures", mcpHealthCheckService.getConsecutiveFailures(),
            "summary", mcpHealthCheckService.getHealthInfo()
        );
    }

}
