package com.incodelabs.alignedexecutionengine.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAiConfig {
    @Bean("openAiChatClient") // should be renamed to action planner
    public ChatClient openAiChatClient(ChatClient.Builder builder, @Autowired(required = false) List<ToolCallbackProvider> allToolProviders) {
        List<ToolCallbackProvider> toolProviders = allToolProviders != null ? allToolProviders : new ArrayList<>();
        return builder
                .defaultSystem("You are helpful assistant that helps users with their tasks.")
                .defaultToolCallbacks(toolProviders.toArray(new ToolCallbackProvider[0]))
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O)
                        .temperature(0.3)
                        .build())
                .build();
    }
}
