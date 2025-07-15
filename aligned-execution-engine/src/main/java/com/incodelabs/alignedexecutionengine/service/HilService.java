package com.incodelabs.alignedexecutionengine.service;

import ch.qos.logback.classic.Logger;
import com.incodelabs.alignedexecutionengine.repository.HilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class HilService {

    private final HilRepository hilRepository;

    // CRUD
    public String createHil(String sessionId, String actionId){
        String hilId = UUID.randomUUID().toString();
        log.info("Creating new hil request with nID: {} for session: {} for action: {}", hilId, sessionId, actionId);
        HilRepository.createHil(sessionId, actionId, hilId);
        return hilId;
    }
}
