package com.incodelabs.alignedexecutionengine.api.http;

import com.incodelabs.alignedexecutionengine.service.QuerySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/querysession")
@RequiredArgsConstructor
public class QuerySessionController {
    private final QuerySessionService querySessionService;

    @GetMapping("/{sessionId}")
    public Map<String, Object> getQuerySessionById(@PathVariable String sessionId) {
        return querySessionService.getQuerySession(sessionId);
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllQuerySessions() {
        return querySessionService.getAllQuerySessions();
    }
}
