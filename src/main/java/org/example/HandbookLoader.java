package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HandbookLoader {

    public static List<Clause> parseJsonToClauses(String jsonString) {
        List<Clause> clauses = new ArrayList<>();
        try {
            String cleaned = jsonString.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json|```", "").trim();
            }

            JSONArray jsonArray;
            if (cleaned.startsWith("{")) {
                JSONObject root = new JSONObject(cleaned);
                String key = root.keySet().iterator().next();
                jsonArray = root.getJSONArray(key);
            } else {
                jsonArray = new JSONArray(cleaned);
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray argsJson = obj.getJSONArray("arguments");
                List<String> args = new ArrayList<>();
                for (int j = 0; j < argsJson.length(); j++) args.add(argsJson.getString(j));

                Literal lit = new Literal(obj.getString("predicate"), obj.getBoolean("isPositive"), args);
                clauses.add(new Clause(List.of(lit)));
            }
        } catch (Exception e) {
            System.err.println("Parser Error: " + e.getMessage());
        }
        return clauses;
    }
}