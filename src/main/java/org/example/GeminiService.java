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

    @Value("${gemini.api.key:}")
    private String apiKey;

    // Updated default to Gemini 2.0 Flash
    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateExplanation(String rulesJson, String scenario) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "{\"status\": \"ERROR\", \"explanation\": \"Backend Error: Gemini API Key is missing.\"}";
        }

        RestTemplate restTemplate = new RestTemplate();

        String promptText = "Analyze this scenario based on these rules. " +
                "Rules: " + rulesJson + " Scenario: " + scenario +
                " Return only a JSON with 'status' and 'explanation'.";

        Map<String, Object> textPart = Map.of("text", promptText);
        Map<String, Object> content = Map.of("parts", Collections.singletonList(textPart));
        Map<String, Object> requestBody = Map.of("contents", Collections.singletonList(content));

        String fullUrl = apiUrl + "?key=" + apiKey.trim();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, requestBody, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            // Extract the text from the Gemini response structure
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
        } catch (Exception e) {
            return "{\"status\": \"ERROR\", \"explanation\": \"Google API Error: " + e.getMessage() + "\"}";
        }
    }
}