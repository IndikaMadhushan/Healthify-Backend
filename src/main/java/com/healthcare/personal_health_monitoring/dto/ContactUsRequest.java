package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactUsRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Size(max = 150, message = "Email must be at most 150 characters")
        String email,

        @Size(max = 30, message = "Phone must be at most 30 characters")
        String phone,

        @Size(max = 200, message = "Subject must be at most 200 characters")
        String subject,

        @NotBlank(message = "Message is required")
        @Size(max = 5000, message = "Message must be at most 5000 characters")
        String message
) {

    public ContactUsRequest normalized() {
        return new ContactUsRequest(
                normalizeSingleLine(name),
                normalizeSingleLine(email),
                normalizeSingleLine(phone),
                normalizeSingleLine(subject),
                trimToNull(message)
        );
    }

    private static String normalizeSingleLine(String value) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            return null;
        }

        return trimmed.replaceAll("[\\r\\n]+", " ");
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
