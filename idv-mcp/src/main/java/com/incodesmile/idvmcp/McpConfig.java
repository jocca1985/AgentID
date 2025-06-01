package com.incodesmile.idvmcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    @Bean
    public ToolCallbackProvider greetingTools(IdvMcpService idvMcpService) {
        return MethodToolCallbackProvider.builder().toolObjects(idvMcpService).build();
    }
}
