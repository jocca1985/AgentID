package com.mcpexample.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

public class LoggingToolCallbackProvider implements ToolCallbackProvider {

    private static final Logger logger = LoggerFactory.getLogger(LoggingToolCallbackProvider.class);
    private final ToolCallbackProvider delegate;

    public LoggingToolCallbackProvider(ToolCallbackProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public ToolCallback[] getToolCallbacks() {
        ToolCallback[] originalCallbacks = delegate.getToolCallbacks();

        // Wrap each callback with logging
        ToolCallback[] loggingCallbacks = new ToolCallback[originalCallbacks.length];
        for (int i = 0; i < originalCallbacks.length; i++) {
            loggingCallbacks[i] = new LoggingToolCallback(originalCallbacks[i]);
        }

        return loggingCallbacks;
    }
}
