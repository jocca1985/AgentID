package com.mcpexample.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;

public class LoggingToolCallback implements ToolCallback {

    private static final Logger logger = LoggerFactory.getLogger(LoggingToolCallback.class);
    private final ToolCallback delegate;

    public LoggingToolCallback(ToolCallback delegate) {
        this.delegate = delegate;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return delegate.getToolDefinition();
    }

    @Override
    public ToolMetadata getToolMetadata() {
        return delegate.getToolMetadata();
    }

    @Override
    public String call(String toolInput) {
        ToolDefinition toolDef = getToolDefinition();

        logger.info("=== RAW TOOL CALL ===");
        logger.info("Tool name: {}", toolDef.name());
        logger.info("Tool description: {}", toolDef.description());
        logger.info("Raw input JSON: {}", toolInput);
        logger.info("Tool input schema: {}", toolDef.inputSchema());
        logger.info("=== END RAW TOOL CALL ===");

        String result = delegate.call(toolInput);

        logger.info("=== TOOL RESULT ===");
        logger.info("Tool: {}", toolDef.name());
        logger.info("Result: {}", result);
        logger.info("=== END TOOL RESULT ===");

        return result;
    }

    @Override
    public String call(String toolInput, ToolContext toolContext) {
        ToolDefinition toolDef = getToolDefinition();

        logger.info("=== RAW TOOL CALL WITH CONTEXT ===");
        logger.info("Tool name: {}", toolDef.name());
        logger.info("Tool description: {}", toolDef.description());
        logger.info("Raw input JSON: {}", toolInput);
        logger.info("Tool context: {}", toolContext != null ? toolContext.getContext() : "null");
        logger.info("Tool input schema: {}", toolDef.inputSchema());
        logger.info("=== END RAW TOOL CALL WITH CONTEXT ===");

        String result = delegate.call(toolInput, toolContext);

        logger.info("=== TOOL RESULT WITH CONTEXT ===");
        logger.info("Tool: {}", toolDef.name());
        logger.info("Result: {}", result);
        logger.info("=== END TOOL RESULT WITH CONTEXT ===");

        return result;
    }
}