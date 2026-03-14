package com.healthcare.personal_health_monitoring.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private final String message;
    private final Map<String, String> errors;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
