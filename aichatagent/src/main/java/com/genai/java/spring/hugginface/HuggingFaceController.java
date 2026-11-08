package com.genai.java.spring.hugginface;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/huggingface/chat")
public class HuggingFaceController {

    private final static String DEFAULT_SYSTEM_PROMPT = "You are helpful. Make sure it's concise, informative, and captures all the key points.";
    private final ChatClient chatClient;

    public HuggingFaceController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/generate-code")
    public ChatResponse generateCode(@RequestBody String message) { // 2. Update return type
        return chatClient.prompt()
                .system(DEFAULT_SYSTEM_PROMPT)
                .user(message)
                .call()
                .chatResponse(); // 3. Use chatResponse() instead of chatClientResponse()
    }
}
