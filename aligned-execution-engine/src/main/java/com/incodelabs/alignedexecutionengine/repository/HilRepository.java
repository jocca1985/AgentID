package com.incodelabs.alignedexecutionengine.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class HilRepository {
    // CRUD
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void createHil(String sessionId, String actionId, String hilId) {

    }
}
