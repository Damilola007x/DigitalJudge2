package org.example; // Ensure this matches your folder structure

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class parseRules { // The class name MUST match the filename

    public static List<LogicalRule> parse(String rawJsonResponse) {
        ObjectMapper mapper = new ObjectMapper();
        List<LogicalRule> rules = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(rawJsonResponse);
            // Navigate the Gemini response structure
            String jsonString = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // Convert the string of rules into actual Java Objects
            JsonNode ruleArray = mapper.readTree(jsonString);
            for (JsonNode node : ruleArray) {
                rules.add(new LogicalRule(
                        node.get("predicate").asText(),
                        node.get("subject").asText(),
                        node.get("object").asText()
                ));
            }
        } catch (Exception e) {
            System.err.println("Failed to parse rules: " + e.getMessage());
        }
        return rules;
    }
}