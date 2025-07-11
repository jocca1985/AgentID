//package com.incodelabs.alignedexecutionengine.repository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//import com.incodelabs.alignedexecutionengine.integration.dto.LogEntry;
//
//import java.sql.PreparedStatement;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//
//@Repository
//public class LoggingRepository {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    // Generic save method
//    public void save(LogEntry logEntry) {
//        // TODO: Implement based on LogEntry structure
//    }
//
//    // === STEP 1: Query Session ===
//    public String createQuerySession(String prompt, String sessionId) {
//        String sql = "INSERT INTO query_sessions (session_id, prompt, start_time) VALUES (?, ?, ?)";
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, sessionId);
//            ps.setString(2, prompt);
//            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
//            return ps;
//        });
//
//        return sessionId;
//    }
//
//    // no sessionID here
//    public void updateQuerySessionPolicyInfo(String sessionId, String policyDecision, String policyTriggered) {
//        String sql = "UPDATE query_sessions SET policy_decision = ?, policy_triggered = ? WHERE session_id = ?";
//        jdbcTemplate.update(sql, policyDecision, policyTriggered, sessionId);
//    }
//
//    public void completeQuerySession(String sessionId) {
//        String sql = "UPDATE query_sessions SET end_time = ? WHERE session_id = ?";
//        jdbcTemplate.update(sql, Timestamp.valueOf(LocalDateTime.now()), sessionId);
//    }
//
//    // === STEP 2: Actions ===
//    public String startAction(String sessionId, Integer loopCount, String actionId) {
//        String sql = "INSERT INTO actions (action_id, session_id, loop_count, start_time) VALUES (?, ?, ?, ?)";
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, actionId);
//            ps.setString(2, sessionId);
//            ps.setInt(3, loopCount);
//            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
//            return ps;
//        });
//
//        return actionId;
//    }
//
//    public void completeAction(String actionId, String plan) {
//        String sql = "UPDATE actions SET plan = ?, end_time = ? WHERE action_id = ?";
//        jdbcTemplate.update(sql, plan, Timestamp.valueOf(LocalDateTime.now()), actionId);
//    }
//
//    // === STEP 3: Policy Checks ===
//    public String startPolicyCheck(String actionId, String policyId) {
//        String sql = "INSERT INTO policy_checks (policy_check_id, action_id, policy_status, start_time) VALUES (?, ?, 'PENDING', ?)";
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, policyId);
//            ps.setString(2, actionId);
//            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
//            return ps;
//        });
//
//        return policyId;
//    }
//
//    public void completePolicyCheck(String policyId, String decision, String policyTriggered) {
//        String sql = "UPDATE policy_checks SET decision = ?, policy_triggered = ?, policy_status = 'COMPLETED', end_time = ? WHERE policy_check_id = ?";
//        jdbcTemplate.update(sql, decision, policyTriggered, Timestamp.valueOf(LocalDateTime.now()), policyId);
//    }
//
//    // === STEP 4: Tool Resources ===
//    public String createToolRequest(String actionId, String toolName, String toolRequestId) {
//        String sql = "INSERT INTO tool_requests (tool_request_id, action_id, policy_id, tool_status, tool_name) VALUES (?, ?, ?, 'PENDING', ?)";
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, toolRequestId);
//            ps.setString(2, actionId);
//            ps.setString(3, null); // policy_id placeholder
//            ps.setString(4, toolName);
//            return ps;
//        });
//
//        return toolRequestId;
//    }
//
//    // Method initiates tool execution, must look for tool request id by filtering through all tools with action id and tool name to match that tool request and return it
//    public String initiateToolExecution(String actionId, String toolName) {
//        String toolRequestId = jdbcTemplate.queryForObject(
//            "SELECT tool_request_id FROM tool_requests WHERE action_id = ? AND tool_name = ?",
//            String.class,
//            actionId, toolName
//        );
//
//        String sql = "UPDATE tool_requests SET tool_status = 'INITIATED', exec_start_time = ? WHERE action_id = ? AND tool_name = ?";
//        jdbcTemplate.update(sql, Timestamp.valueOf(LocalDateTime.now()), actionId, toolName);
//
//        return toolRequestId;
//    }
//
//    public void completeToolExecution(String toolRequestId, String finalStatus) {
//        String sql = "UPDATE tool_requests SET tool_status = ?, exec_end_time = ? WHERE tool_request_id = ?";
//        jdbcTemplate.update(sql, finalStatus, Timestamp.valueOf(LocalDateTime.now()), toolRequestId);
//    }
//
//    // === STEP 5: Feedback ===
//    public void startFeedbackRequest(String actionId) {
//        String sql = "INSERT INTO feedback (action_id, start_time) VALUES (?, ?)";
//        jdbcTemplate.update(sql, actionId, Timestamp.valueOf(LocalDateTime.now()));
//    }
//
//    public void completeFeedbackRequest(String actionId, String feedback, String decision) {
//        String sql = "UPDATE feedback SET feedback = ?, decision = ?, end_time = ? WHERE action_id = ?";
//        jdbcTemplate.update(sql, feedback, decision, Timestamp.valueOf(LocalDateTime.now()), actionId);
//    }
//
//    // === UTILITY METHODS ===
//    public void updateToolStatus(String toolRequestId, String status) {
//        String sql = "UPDATE tool_requests SET tool_status = ? WHERE tool_request_id = ?";
//        jdbcTemplate.update(sql, status, toolRequestId);
//    }
//
//    // Query methods for checking state
//    public String getToolStatus(String toolRequestId) {
//        String sql = "SELECT tool_status FROM tool_requests WHERE tool_request_id = ?";
//        return jdbcTemplate.queryForObject(sql, String.class, toolRequestId);
//    }
//
//    public String getPolicyStatus(String policyId) {
//        String sql = "SELECT policy_status FROM policy_checks WHERE policy_check_id = ?";
//        return jdbcTemplate.queryForObject(sql, String.class, policyId);
//    }
//}