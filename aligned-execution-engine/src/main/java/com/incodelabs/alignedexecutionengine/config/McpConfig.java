package com.incodelabs.alignedexecutionengine.config;

import com.incodelabs.alignedexecutionengine.api.mcp.PromptExecutorMcpService;
import com.incodelabs.alignedexecutionengine.service.AsyncProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class McpConfig {
    
    @Bean
    @Lazy
    public PromptExecutorMcpService promptExecutorMcpService(@Lazy AsyncProcessingService asyncProcessingService) {
        return new PromptExecutorMcpService(asyncProcessingService);
    }
    
    @Bean("localMcpTools")
    public ToolCallbackProvider localMcpTools(@Lazy PromptExecutorMcpService mcp) {
        return MethodToolCallbackProvider.builder().toolObjects(mcp).build();
    }
    
    @Bean("mcpChatClient")
    public ChatClient mcpChatClient(ChatClient.Builder builder, @Autowired(required = false) List<ToolCallbackProvider> allToolProviders) {
        
        List<ToolCallbackProvider> toolProviders = allToolProviders != null ? allToolProviders : new ArrayList<>();
        
        log.info("Found {} ToolCallbackProvider beans", toolProviders.size());
        for (int i = 0; i < toolProviders.size(); i++) {
            ToolCallbackProvider provider = toolProviders.get(i);
            var toolCallbacks = provider.getToolCallbacks();
            log.info("Provider {}: {} with {} tools", i, provider.getClass().getSimpleName(), 
                    toolCallbacks.length);
            for (var tool : toolCallbacks) {
                log.info("  - Tool: {}", tool.getClass().getSimpleName());
            }
        }
        
        var chatClientBuilder = builder
                .defaultSystem("You are a helpful assistant that can execute banking operations, check balances, and handle various requests. Use available tools when appropriate.");
        
        // Add all available tool providers (both local MCP server tools and external MCP client tools)
        chatClientBuilder.defaultToolCallbacks(toolProviders.toArray(new ToolCallbackProvider[0]));
        
        return chatClientBuilder.build();
    }
}
