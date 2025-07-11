package com.incodelabs.alignedexecutionengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncProcessingService {
    
    @Lazy
    private final ActionControllerService actionControllerService;
    private final Map<String, ProcessingSession> sessions = new ConcurrentHashMap<>();
    
    public static class ProcessingSession {
        private final String sessionId;
        private final String prompt;
        private final long startTime;
        private ProcessingStatus status;
        private ActionFeedbackResponse result;
        private String errorMessage;
        
        public ProcessingSession(String sessionId, String prompt) {
            this.sessionId = sessionId;
            this.prompt = prompt;
            this.startTime = System.currentTimeMillis();
            this.status = ProcessingStatus.PROCESSING;
        }
        
        // Getters and setters
        public String getSessionId() { return sessionId; }
        public String getPrompt() { return prompt; }
        public long getStartTime() { return startTime; }
        public ProcessingStatus getStatus() { return status; }
        public void setStatus(ProcessingStatus status) { this.status = status; }
        public ActionFeedbackResponse getResult() { return result; }
        public void setResult(ActionFeedbackResponse result) { this.result = result; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
    
    public enum ProcessingStatus {
        PROCESSING, COMPLETED, FAILED
    }
    
    /**
     * Start async processing and return session ID
     */
    public String startAsyncProcessing(String prompt) {
        String sessionId = UUID.randomUUID().toString();
        ProcessingSession session = new ProcessingSession(sessionId, prompt);
        sessions.put(sessionId, session);
        
        log.info("Starting async processing for session: {}", sessionId);
        
        // Use CompletableFuture to ensure truly async execution
        CompletableFuture.runAsync(() -> {
            try {
                log.info("Processing prompt for session {}: {}", sessionId, prompt);
                ActionFeedbackResponse result = actionControllerService.testControllerAgent(prompt, sessionId);
                
                session.setResult(result);
                session.setStatus(ProcessingStatus.COMPLETED);
                log.info("Completed processing for session: {}", sessionId);
                
            } catch (Exception e) {
                log.error("Error processing session: {}", sessionId, e);
                session.setErrorMessage("Processing failed: " + e.getMessage());
                session.setStatus(ProcessingStatus.FAILED);
            }
        });
        
        return sessionId;
    }
    
    /**
     * Get processing session by ID
     */
    public ProcessingSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
    
    /**
     * Get all sessions (for debugging/monitoring)
     */
    public Map<String, ProcessingSession> getAllSessions() {
        return sessions;
    }
    
    /**
     * Clean up old sessions (sessions older than 1 hour)
     */
    public void cleanupOldSessions() {
        long cutoffTime = System.currentTimeMillis() - (60 * 60 * 1000); // 1 hour ago
        sessions.entrySet().removeIf(entry -> entry.getValue().getStartTime() < cutoffTime);
    }
    
}