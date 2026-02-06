package org.example;

import java.util.*;

public class Unifier {

    /**
     * Attempts to unify two Literals.
     * Returns a Map of variable substitutions if successful, or null if they cannot unify.
     */
    public static Map<String, String> unify(Literal l1, Literal l2) {
        // Use the updated method names from your Literal class
        if (!l1.getPredicate().equals(l2.getPredicate()) ||
                l1.getArguments().size() != l2.getArguments().size()) {
            return null;
        }

        Map<String, String> substitution = new HashMap<>();
        for (int i = 0; i < l1.getArguments().size(); i++) {
            String arg1 = l1.getArguments().get(i);
            String arg2 = l2.getArguments().get(i);

            if (!unifyTerms(arg1, arg2, substitution)) {
                return null;
            }
        }
        return substitution;
    }

    private static boolean unifyTerms(String s1, String s2, Map<String, String> subst) {
        // Recursively find what the variable currently stands for
        while (subst.containsKey(s1)) s1 = subst.get(s1);
        while (subst.containsKey(s2)) s2 = subst.get(s2);

        if (s1.equals(s2)) return true;

        if (isVariable(s1)) {
            subst.put(s1, s2);
            return true;
        } else if (isVariable(s2)) {
            subst.put(s2, s1);
            return true;
        }

        return false;
    }

    /**
     * Simple Variable Check:
     * In formal logic, variables are typically single lowercase letters (x, y, z).
     * Constants (Alice, Today) start with Uppercase.
     */
    private static boolean isVariable(String s) {
        if (s == null || s.isEmpty()) return false;
        return Character.isLowerCase(s.charAt(0));
    }
}