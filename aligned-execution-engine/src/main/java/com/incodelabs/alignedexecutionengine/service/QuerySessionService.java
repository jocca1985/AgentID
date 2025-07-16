
package com.incodelabs.alignedexecutionengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.incodelabs.alignedexecutionengine.repository.QuerySessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuerySessionService {

    private final QuerySessionRepository querySessionRepository;
    
    // CRUD operations for query sessions, includes what used to be in loggingService for query sessions
    /**
     * Creates a new query session with a unique session ID
     * @return The generated session ID
     */
    public String createQuerySession() {
        String sessionId = UUID.randomUUID().toString();
        log.info("Created new query session with ID: {}", sessionId);
        return sessionId;
    }

    
    /**
     * Starts a query session with the given prompt and session ID
     * @param prompt The initial prompt for the session
     * @param sessionId The session ID to associate with this query
     */
    public void startQuerySession(String prompt, String sessionId) {
        log.info("Starting query session {} with prompt: {}", sessionId, prompt);
        // Calls QuerySessionRepository to create a new query session
        querySessionRepository.createQuerySession(sessionId, prompt);

    }
    
    /**
     * Updates the session policy status
     * @param sessionId The session ID to update
     * @param policy The policy status (allow, deny, idv, failed)
     * @param details Additional details about the policy decision
     */
    public void updateSessionPolicy(String sessionId, String policy, String details) {
        log.info("Updating session {} policy to: {} - {}", sessionId, policy, details);
        // Calls QuerySessionRepository to update the session policy
        querySessionRepository.updateQuerySessionPolicyInfo(sessionId, policy, details);
    }
    
    /**
     * Ends a query session
     * @param sessionId The session ID to end
     */
    public void endQuerySession(String sessionId) {
        log.info("Ending query session: {}", sessionId);
        // Calls QuerySessionRepository to end the session
        querySessionRepository.endQuerySession(sessionId);
    }
    
    /**
     * Retrieves a query session by ID
     * @param sessionId The session ID to retrieve
     * @return The query session data (implement based on your data model)
     */
    public Map<String, Object> getQuerySession(String sessionId) {
        log.info("Retrieving query session: {}", sessionId);
        // Calls QuerySessionRepository to retrieve the session
        return querySessionRepository.getQuerySession(sessionId);
    }
    
    /**
     * Lists all query sessions
     * @return List of all query sessions
     */
    public List<Map<String, Object>> getAllQuerySessions() {
        log.info("Retrieving all query sessions");
        // Calls QuerySessionRepository to retrieve all sessions
        return querySessionRepository.getAllQuerySessions();    
    }
    
    /**
     * Deletes a query session by ID
     * @param sessionId The session ID to delete
     */
    public void deleteQuerySession(String sessionId) {
        log.info("Deleting query session: {}", sessionId);
        // Calls QuerySessionRepository to delete the session
        querySessionRepository.deleteQuerySession(sessionId);
    }
    
    /**
     * Updates a query session with new data
     * @param sessionId The session ID to update
     * @param sessionData The new session data
     */
    public void updateQuerySession(String sessionId, Object sessionData) {
        log.info("Updating query session: {}", sessionId);
        // Calls QuerySessionRepository to update the session
        querySessionRepository.updateQuerySession(sessionId, sessionData);
    }
    
    /**
     * Gets the current timestamp for session tracking
     * @return Current timestamp
     */
    public LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }

    /**
     * Gets the paused flag for a session
     * @param sessionId The session ID to retrieve
     * @return The paused flag
     */
    public Boolean getQuerySessionPausedBoolean(String sessionId) {
        return querySessionRepository.getQuerySessionPausedBoolean(sessionId);
    }

    /**
     * Pauses a query session by setting the paused flag to true
     * @param sessionId The session ID to pause
     */
    public void pauseQuerySession(String sessionId) {
        querySessionRepository.pauseQuerySession(sessionId);
    }

    /**
     * Resumes a query session by setting the paused flag to false
     * @param sessionId The session ID to resume
     */
    public void resumeQuerySession(String sessionId) {
        querySessionRepository.resumeQuerySession(sessionId);
    }
}
