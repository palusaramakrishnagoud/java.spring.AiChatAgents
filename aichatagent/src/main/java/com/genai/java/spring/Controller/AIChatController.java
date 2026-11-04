package com.genai.java.spring.Controller;

import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anthropicai/chat")
public class AIChatController {

    private final static String DEFAULT_SYSTEM_PROMPT = "Your helpful."
            + "Make sure its concise informative and capture all the key points.";

    private final ChatClient chatClient;

    public AIChatController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/summary")
    public String summary(@RequestParam String message) {
        return chatClient.prompt()
                .system(DEFAULT_SYSTEM_PROMPT)
                .user(message)
                .call()
                .content();

    }

    @PostMapping("/summarize-notes")
    public String summarizeNotes(@RequestParam String notes) {
        return chatClient.prompt()
                .system(DEFAULT_SYSTEM_PROMPT)
                .user(u -> u.text("Can you summarize the following meeting notes: {notes} " +
                        "Use the format as described in the following example while doing the summarizing "+
                        "Input: In today's sales strategy meeting, we reviewed Q3 targets and performance of mobile division"+
                        "A proposal was made to expand into two new regions. "+
                        "Output:" +
                                " Action Items:" +
                                "* Focus on enterprise clients and partnerships.")
                        .param("meetingNotes", notes))
                .call()
                .content();
    }
}
