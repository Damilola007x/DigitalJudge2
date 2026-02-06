package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compliance")
@CrossOrigin(origins = "*")
public class ComplianceController {

    private final HandbookGenerator handbookGenerator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ComplianceController(HandbookGenerator handbookGenerator) {
        this.handbookGenerator = handbookGenerator;
    }

    @PostMapping("/audit")
    public ResponseEntity<AuditResponse> runAudit(
            @RequestParam("file") MultipartFile file,
            @RequestParam("scenario") String scenario) {

        Path tempFile = null;
        try {
            // 1. PDF Text Extraction
            tempFile = Files.createTempFile("audit-", ".pdf");
            file.transferTo(tempFile.toFile());
            String rawText = PDFExtractor.extractText(tempFile.toString());

            // 2. AI Translation (For the Symbolic Engine)
            String rulesJson = handbookGenerator.translateHandbookToLogic(rawText);

            // 3. Get Structured AI Verdict (The "Neural" decision)
            // This returns a JSON like {"status": "VIOLATION", "explanation": "..."}
            String aiRawResponse = handbookGenerator.generateExplanation(rulesJson, scenario);

            JsonNode responseJson = objectMapper.readTree(aiRawResponse);
            String status = responseJson.path("status").asText("COMPLIANT");
            String explanation = responseJson.path("explanation").asText("No explanation provided.");
            boolean isViolation = status.equalsIgnoreCase("VIOLATION");

            // 4. (Optional) Still parse for Knowledge Base size count
            List<Clause> kb = new ArrayList<>();
            try {
                kb.addAll(HandbookLoader.parseJsonToClauses(rulesJson));
            } catch (Exception ignored) {}

            return ResponseEntity.ok(new AuditResponse(
                    true,
                    isViolation,
                    status,
                    explanation,
                    kb.size()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new AuditResponse(false, false, "ERROR", "Internal Server Error: " + e.getMessage(), 0));
        } finally {
            if (tempFile != null) try { Files.deleteIfExists(tempFile); } catch (Exception ignored) {}
        }
    }
}