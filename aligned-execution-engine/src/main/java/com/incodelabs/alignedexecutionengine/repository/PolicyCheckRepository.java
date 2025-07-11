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
public class PolicyCheckRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create a new policy check
    public void createPolicyCheck(String policyId, String actionId) {
        String sql = "INSERT INTO policy_checks (policy_check_id, action_id, policy_status, start_time) VALUES (?, ?, 'PENDING', ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, policyId);
            ps.setString(2, actionId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        });
    }

    // Retrieve a policy check by policyId
    public Map<String, Object> getPolicyCheck(String policyId) {
        String sql = "SELECT * FROM policy_checks WHERE policy_check_id = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, policyId);
        return results.isEmpty() ? null : results.get(0);
    }

    // Retrieve all policy checks for an action
    public List<Map<String, Object>> getPolicyChecksByAction(String actionId) {
        String sql = "SELECT * FROM policy_checks WHERE action_id = ? ORDER BY start_time DESC";
        return jdbcTemplate.queryForList(sql, actionId);
    }

    // Complete a policy check with decision and policy triggered
    public void completePolicyCheck(String policyId, String status, String decision, String policyTriggered) {
        String sql = "UPDATE policy_checks SET policy_status = ?, decision = ?, policy_triggered = ?, end_time = ? WHERE policy_check_id = ?";
        jdbcTemplate.update(sql, status, decision, policyTriggered, Timestamp.valueOf(LocalDateTime.now()), policyId);
    }

    // Update policy status
    public void updatePolicyStatus(String policyId, String status, String decision, String policyTriggered) {
        String sql = "UPDATE policy_checks SET policy_status = ?, decision = ?, policy_triggered = ? WHERE policy_check_id = ?";
        jdbcTemplate.update(sql, status, decision, policyTriggered, policyId);
    }

    // Retrieve all policy checks for an action plan
    public List<Map<String, Object>> getPolicyChecksByActionPlan(String actionPlanId) {
        String sql = "SELECT * FROM policy_checks WHERE action_id = ? ORDER BY start_time DESC";
        return jdbcTemplate.queryForList(sql, actionPlanId);
    }

    // Get policy status
    public String getPolicyStatus(String policyId) {
        String sql = "SELECT policy_status FROM policy_checks WHERE policy_check_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, policyId);
    }

    // Delete a policy check by policyId
    public void deletePolicyCheck(String policyId) {
        String sql = "DELETE FROM policy_checks WHERE policy_check_id = ?";
        jdbcTemplate.update(sql, policyId);
    }
}
