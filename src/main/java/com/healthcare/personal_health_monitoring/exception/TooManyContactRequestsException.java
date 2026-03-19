package com.healthcare.personal_health_monitoring.exception;

public class TooManyContactRequestsException extends RuntimeException {

    public TooManyContactRequestsException(String message) {
        super(message);
    }
}
