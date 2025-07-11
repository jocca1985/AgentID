package com.incodelabs.alignedexecutionengine.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class QuerySessionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Creates a new query session
     * @param sessionId The session ID
     * @param prompt The initial prompt
     */
    public void createQuerySession(String sessionId, String prompt) {
        String sql = "INSERT INTO query_sessions (session_id, prompt, start_time) VALUES (?, ?, ?)";
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, sessionId);
            ps.setString(2, prompt);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        });
    }

    /**
     * Retrieves a query session by ID
     * @param sessionId The session ID to retrieve
     * @return The query session data as a Map
     */
    public Map<String, Object> getQuerySession(String sessionId) {
        String sql = "SELECT * FROM query_sessions WHERE session_id = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, sessionId);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Retrieves all query sessions
     * @return List of all query sessions
     */
    public List<Map<String, Object>> getAllQuerySessions() {
        String sql = "SELECT * FROM query_sessions ORDER BY start_time DESC";
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Updates a query session
     * @param sessionId The session ID to update
     * @param sessionData The new session data
     */
    public void updateQuerySession(String sessionId, Object sessionData) {
        // This is a generic update method - you may want to make it more specific
        // based on your actual session data structure
        String sql = "UPDATE query_sessions SET prompt = ? WHERE session_id = ?";
        jdbcTemplate.update(sql, sessionData.toString(), sessionId);
    }

    /**
     * Deletes a query session by ID
     * @param sessionId The session ID to delete
     */
    public void deleteQuerySession(String sessionId) {
        String sql = "DELETE FROM query_sessions WHERE session_id = ?";
        jdbcTemplate.update(sql, sessionId);
    }

    /**
     * Updates the session policy information
     * @param sessionId The session ID
     * @param policyDecision The policy decision
     * @param policyTriggered The policy that was triggered
     */
    public void updateQuerySessionPolicyInfo(String sessionId, String policyDecision, String policyTriggered) {
        String sql = "UPDATE query_sessions SET policy_decision = ?, policy_triggered = ? WHERE session_id = ?";
        jdbcTemplate.update(sql, policyDecision, policyTriggered, sessionId);
    }

    /**
     * Completes a query session by setting the end time
     * @param sessionId The session ID to complete
     */
    public void endQuerySession(String sessionId) {
        String sql = "UPDATE query_sessions SET end_time = ? WHERE session_id = ?";
        jdbcTemplate.update(sql, Timestamp.valueOf(LocalDateTime.now()), sessionId);
    }
}