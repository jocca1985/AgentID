package com.incodelabs.alignedexecutionengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class McpClientService {
    @Qualifier("mcpChatClient")
    private final ChatClient mcpChatClient;

    public String checkBalance() {
        try {
            log.info("Calling check-balance tool via ChatClient with MCP integration");
            
            String response = mcpChatClient.prompt()
                    .user("Check my account balance using the check-balance tool")
                    .call()
                    .content();
            
            log.info("MCP tool response via ChatClient: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error calling check-balance tool via ChatClient", e);
            return "Error calling check-balance tool: " + e.getMessage();
        }
    }
    
    /**
     * Execute specific MCP tool with parameters.
     * This method ONLY executes tools and returns results - no additional processing.
     */
    public String executeTool(String toolInstruction) {
        try {
            log.info("Executing MCP tool instruction: {}", toolInstruction);
            
            String response = mcpChatClient.prompt()
                    .system("You are a tool executor. Execute ONLY the requested tool with the provided parameters. " +
                            "Do not explain, interpret, or add commentary. Return only the direct tool execution result.")
                    .user(toolInstruction)
                    .call()
                    .content();
            
            log.info("MCP tool execution completed: {}", toolInstruction);
            return response;
        } catch (Exception e) {
            log.error("Error executing MCP tool: {}", toolInstruction, e);
            return "Error executing tool: " + e.getMessage();
        }
    }
}