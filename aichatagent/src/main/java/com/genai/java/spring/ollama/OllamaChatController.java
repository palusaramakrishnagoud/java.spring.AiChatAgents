package com.genai.java.spring.ollama;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ollama/chat")
public class OllamaChatController {
    private final static String DEFAULT_SYSTEM_PROMPT = "You are very good at drafting a professional email."
            + "Make sure its concise and tailored to specific content.";

    private final ChatClient chatClient;

    public OllamaChatController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
@PostMapping("/draft-email")
    public String emailDraft(@RequestBody String message) {
        return chatClient.prompt()
                .system(DEFAULT_SYSTEM_PROMPT)
                .user(message)
                .call()
                .content();

    }


}
