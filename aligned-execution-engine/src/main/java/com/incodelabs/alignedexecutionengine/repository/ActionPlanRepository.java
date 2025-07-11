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
public class ActionPlanRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create a new action plan (action)
    public void createActionPlan(String actionId, String sessionId, int loopCount) {
        String sql = "INSERT INTO actions (action_id, session_id, loop_count, start_time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, actionId);
            ps.setString(2, sessionId);
            ps.setInt(3, loopCount);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        });
    }

    // Retrieve an action plan by actionId
    public Map<String, Object> getActionPlan(String actionId) {
        String sql = "SELECT * FROM actions WHERE action_id = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, actionId);
        return results.isEmpty() ? null : results.getFirst();
    }

    // Retrieve all action plans for a session
    public List<Map<String, Object>> getActionPlansBySession(String sessionId) {
        String sql = "SELECT * FROM actions WHERE session_id = ? ORDER BY start_time DESC";
        return jdbcTemplate.queryForList(sql, sessionId);
    }

    // Update an action plan (e.g., set plan or end_time)
    public void updateActionPlan(String actionId, String plan) {
        String sql = "UPDATE actions SET plan = ?, end_time = ? WHERE action_id = ?";
        jdbcTemplate.update(sql, plan, Timestamp.valueOf(LocalDateTime.now()), actionId);
    }

    // Delete an action plan by actionId
    public void deleteActionPlan(String actionId) {
        String sql = "DELETE FROM actions WHERE action_id = ?";
        jdbcTemplate.update(sql, actionId);
    }
}
