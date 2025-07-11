package com.incodelabs.alignedexecutionengine.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class FeedbackRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create a new feedback request
    public void createFeedbackRequest(String actionId) {
        String sql = "INSERT INTO feedback (action_id, start_time) VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, actionId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        });
    }

    // Retrieve a feedback request by actionId
    public Map<String, Object> getFeedbackRequest(String actionId) {
        String sql = "SELECT * FROM feedback WHERE action_id = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, actionId);
        return results.isEmpty() ? null : results.get(0);
    }

    // Retrieve all feedback requests
    public List<Map<String, Object>> getAllFeedbackRequests() {
        String sql = "SELECT * FROM feedback ORDER BY start_time DESC";
        return jdbcTemplate.queryForList(sql);
    }

    // Retrieve all feedback requests by actionId
    public List<Map<String, Object>> getFeedbackByAction(String actionId) {
        String sql = "SELECT * FROM feedback WHERE action_id = ?";
        return jdbcTemplate.queryForList(sql, actionId);
    }

    // Retrieve all feedback requests by actionPlanId
    public List<Map<String, Object>> getFeedbackByActionPlan(String actionPlanId) {
        String sql = "SELECT * FROM feedback WHERE action_id = ?";
        return jdbcTemplate.queryForList(sql, actionPlanId);
    }

    // Complete a feedback request with feedback and decision
    public void completeFeedbackRequest(String actionId, String feedback, String decision) {
        String sql = "UPDATE feedback SET feedback = ?, decision = ?, end_time = ? WHERE action_id = ?";
        jdbcTemplate.update(sql, feedback, decision, Timestamp.valueOf(LocalDateTime.now()), actionId);
    }

    // Update feedback content
    public void updateFeedback(String actionId, String feedback) {
        String sql = "UPDATE feedback SET feedback = ? WHERE action_id = ?";
        jdbcTemplate.update(sql, feedback, actionId);
    }

    // Update decision
    public void updateDecision(String actionId, String decision) {
        String sql = "UPDATE feedback SET decision = ? WHERE action_id = ?";
        jdbcTemplate.update(sql, decision, actionId);
    }

    // Delete a feedback request by actionId
    public void deleteFeedbackRequest(String actionId) {
        String sql = "DELETE FROM feedback WHERE action_id = ?";
        jdbcTemplate.update(sql, actionId);
    }
}
