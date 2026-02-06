package org.example;

public class AuditResponse {
    private boolean success;
    private boolean isViolation;
    private String status;
    private String explanation;
    private int rulesCount;

    public AuditResponse(boolean success, boolean isViolation, String status, String explanation, int rulesCount) {
        this.success = success;
        this.isViolation = isViolation;
        this.status = status;
        this.explanation = explanation;
        this.rulesCount = rulesCount;
    }

    // Getters and Setters (Required for JSON conversion)
    public boolean isSuccess() { return success; }
    public boolean isIsViolation() { return isViolation; }
    public String getStatus() { return status; }
    public String getExplanation() { return explanation; }
    public int getRulesCount() { return rulesCount; }
}