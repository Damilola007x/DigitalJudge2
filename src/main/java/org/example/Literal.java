package org.example;

import java.util.List;

public class Literal {
    private String predicate;
    private boolean isPositive;
    private List<String> arguments; // Changed from String[] to List

    // Updated Constructor
    public Literal(String predicate, boolean isPositive, List<String> arguments) {
        this.predicate = predicate;
        this.isPositive = isPositive;
        this.arguments = arguments;
    }

    // Getters
    public String getPredicate() { return predicate; }
    public boolean isPositive() { return isPositive; }
    public List<String> getArguments() { return arguments; }

    @Override
    public String toString() {
        return (isPositive ? "" : "¬") + predicate + "(" + String.join(", ", arguments) + ")";
    }
}