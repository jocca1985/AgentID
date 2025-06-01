package com.incodelabs.alignedexecutionengine.api.http;

import com.incodelabs.alignedexecutionengine.service.ActionControllerService;
import com.incodelabs.alignedexecutionengine.service.AsyncProcessingService;
import com.incodelabs.alignedexecutionengine.service.McpClientService;
import com.incodelabs.alignedexecutionengine.service.McpHealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
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
