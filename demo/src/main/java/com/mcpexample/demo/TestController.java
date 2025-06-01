package com.mcpexample.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Collections;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, String> getTestMessage() {
        return Collections.singletonMap("message", "hello");
    }
}