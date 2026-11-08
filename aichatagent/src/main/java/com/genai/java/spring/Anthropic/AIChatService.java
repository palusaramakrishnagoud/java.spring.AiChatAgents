package com.genai.java.spring.Anthropic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;
import org.apache.hc.core5.http.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class AIChatService {
    private static final String ANTHROPIC_API_KEY="ANTHROPIC_API_KEY";
    private static final String ANTHROPIC_API_URL="https://api.anthropic.com/v1/chat/completion";
    private static final String ANTHROPIC_API_MODEL="claude-haiku-4-5";
    private static final String CONTENT_TYPE="application/json";
    private static final String DEFAULT_SYSTEM_PROMPT = "Your helpful."
            + "Make sure its concise informative and capture all the key points.";

    private final ObjectMapper objectMapper;

    // Constructor
    public AIChatService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String chat(String message) throws AIChatException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            var request = getRequest(chat(message));
            var response = httpClient.execute(request, resp -> EntityUtils.toString(resp.getEntity()));
            return parseResponse(response);
        } catch (IOException e) {
            throw new AIChatException("Could not reach Anthropic API using Java client", e);
        }
    }
        private HttpPost getRequest(String prompt) throws JsonProcessingException {
            var request = new HttpPost(ANTHROPIC_API_URL);
            var anthropicApiKey = System.getenv(ANTHROPIC_API_KEY);
            request.addHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer" + anthropicApiKey);

            Map<String, Object> userMessage = Map.of(
                    "role", "system",
                    "content", prompt
            );
            Map<String, Object> systemMessage = Map.of(
                    "role", "system",
                    "content", DEFAULT_SYSTEM_PROMPT
            );
            Map<String, Object> body = Map.of(
                    "role", ANTHROPIC_API_MODEL,
                    "content", List.of(userMessage, systemMessage)
            );
            String requestBody = objectMapper.writeValueAsString(body);
            request.setEntity(new StringEntity(requestBody));
            return request;
        }

        private String parseResponse(String response) throws JsonProcessingException {
             Map anthropicAIResponse = objectMapper.readValue(response,Map.class);
             return anthropicAIResponse.get("choices").toString();
        }



}
