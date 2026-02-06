package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogicalRule {
    @JsonProperty("predicate")
    private String predicate;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("object")
    private String object;

    // Default constructor for Jackson
    public LogicalRule() {}

    public LogicalRule(String predicate, String subject, String object) {
        this.predicate = predicate;
        this.subject = subject;
        this.object = object;
    }

    // Getters and Setters
    public String getPredicate() { return predicate; }
    public void setPredicate(String predicate) { this.predicate = predicate; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }
}