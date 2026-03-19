package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContactUsResponse(
        boolean success,
        String message,
        Map<String, String> errors
) {

    public static ContactUsResponse success(String message) {
        return new ContactUsResponse(true, message, null);
    }

    public static ContactUsResponse failure(String message) {
        return new ContactUsResponse(false, message, null);
    }

    public static ContactUsResponse validationFailure(Map<String, String> errors) {
        return new ContactUsResponse(false, "Validation failed", errors);
    }
}
