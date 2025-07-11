//// To add in store mcp.
//package com.incodelabs.alignedexecutionengine.config;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.tool.ToolCallbackProvider;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class StoreMcpConfig {
//
//    @Value("${spring.ai.mcp.client.sse-url}")
//    private String sseUrl;
//
//    @Bean("storeMcpTools")
//    public ToolCallbackProvider storeMcpTools(RestTemplate restTemplate) {
//        // Create a tool provider that connects to the store MCP SSE endpoint
//        return new SseToolCallbackProvider(sseUrl, restTemplate);
//    }
//
//    @Bean("storeMcpChatClient")
//    public ChatClient storeMcpChatClient(ChatClient.Builder builder,
//                                         @Qualifier("storeMcpTools") ToolCallbackProvider storeMcpTools) {
//        return builder
//                .defaultSystem("You are a helpful assistant that can help with shopping and product queries.")
//                .defaultToolCallbacks(storeMcpTools)
//                .build();
//    }
//}