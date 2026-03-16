package com.healthcare.personal_health_monitoring.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class NameUtil {

    private NameUtil() {
    }

    public static NameParts split(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return new NameParts(null, null, null);
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) {
            return new NameParts(parts[0], null, null);
        }
        if (parts.length == 2) {
            return new NameParts(parts[0], null, parts[1]);
        }

        String secondName = Arrays.stream(parts, 1, parts.length - 1)
                .collect(Collectors.joining(" "));
        return new NameParts(parts[0], secondName, parts[parts.length - 1]);
    }

    public static String combine(String firstName, String secondName, String lastName) {
        return Arrays.asList(firstName, secondName, lastName).stream()
                .filter(part -> part != null && !part.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(" "));
    }

    public record NameParts(String firstName, String secondName, String lastName) {
    }
}
