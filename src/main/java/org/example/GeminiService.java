package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateExplanation(String rulesJson, String scenario) {
        RestTemplate restTemplate = new RestTemplate();

        // Build the prompt
        String promptText = "Analyze this scenario based on these rules. " +
                "Rules: " + rulesJson + " Scenario: " + scenario +
                " Return only a JSON with 'status' and 'explanation'.";

        // Construct Request Body
        Map<String, Object> textPart = Map.of("text", promptText);
        Map<String, Object> content = Map.of("parts", Collections.singletonList(textPart));
        Map<String, Object> requestBody = Map.of("contents", Collections.singletonList(content));

        String fullUrl = apiUrl + "?key=" + apiKey.trim();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, requestBody, String.class);

            // Navigate the Google response tree
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
        } catch (Exception e) {
            // This prevents the "Unexpected character" crash by returning the actual error
            return "{\"status\": \"ERROR\", \"explanation\": \"Google API Error: " + e.getMessage() + "\"}";
        }
    }
}