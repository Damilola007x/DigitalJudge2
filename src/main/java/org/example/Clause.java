package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Clause {
    private List<Literal> literals;

    // Main constructor for multiple literals
    public Clause(List<Literal> literals) {
        this.literals = new ArrayList<>(literals);
    }

    // Helper constructor for a single literal (used in the Prover/Controller)
    public Clause(Literal literal) {
        this.literals = new ArrayList<>();
        this.literals.add(literal);
    }

    public List<Literal> getLiterals() {
        return literals;
    }

    @Override
    public String toString() {
        if (literals.isEmpty()) return "□ (Empty Clause)";
        return literals.stream()
                .map(Literal::toString)
                .collect(Collectors.joining(" ∨ "));
    }
}