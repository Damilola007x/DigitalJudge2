package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class HandbookGenerator {

    @Value("${gemini.api.key}")
    private String apiKey;

    // Stable endpoint for 2026
    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    private String callGemini(String prompt, boolean isJsonMode) {
        // Clean the key from any accidental quotes or spaces
        String cleanKey = apiKey.trim().replace("\"", "").replace("'", "");

        // Put key in URL for maximum compatibility
        String finalUrl = BASE_URL + "?key=" + cleanKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> contents = Map.of("parts", List.of(textPart));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(contents));

        if (isJsonMode) {
            requestBody.put("generationConfig", Map.of(
                    "response_mime_type", "application/json",
                    "temperature", 0.1
            ));
        }

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(finalUrl, entity, Map.class);

            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> contentParts = (List<Map<String, Object>>) content.get("parts");

            return (String) contentParts.get(0).get("text");

        } catch (Exception e) {
            // This is crucial: check your "Run" tab in IntelliJ to see what prints here!
            System.err.println("--- GEMINI CONNECTION ERROR ---");
            System.err.println(e.getMessage());
            return "{\"status\": \"ERROR\", \"explanation\": \"Check IntelliJ Console for details.\"}";
        }
    }

    public String translateHandbookToLogic(String pdfText) {
        return callGemini("Convert to logic rules: " + pdfText, true);
    }

    public String translateScenarioToFacts(String scenarioText) {
        return callGemini("Convert to facts: " + scenarioText, true);
    }

    public String generateExplanation(String rules, String scenario) {
        String prompt = "Analyze scenario vs rules. Rules: " + rules + " Scenario: " + scenario +
                " Return JSON: {\"status\": \"VIOLATION\" or \"COMPLIANT\", \"explanation\": \"...\"}";
        return callGemini(prompt, true);
    }
}