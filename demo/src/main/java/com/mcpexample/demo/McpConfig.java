package com.mcpexample.demo;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    @Bean
    public ToolCallbackProvider greetingTools(BankMcpService bankMcpService) {
        var original = MethodToolCallbackProvider.builder().toolObjects(bankMcpService).build();
//        return new LoggingToolCallbackProvider(original);
        return original;
    }
}