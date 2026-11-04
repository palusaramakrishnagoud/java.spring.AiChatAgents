package com.genai.java.spring.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIChatProviderConfig {

    @Bean("anthropicChatClient")
    ChatClient anthropicChatClient(AnthropicChatModel anthropicChatModel) {
        return ChatClient.builder(anthropicChatModel).build();
    }
}
