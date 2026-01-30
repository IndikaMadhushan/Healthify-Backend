package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(
        @NotBlank String email,
        @NotBlank String otp,
        @NotBlank String newPassword
) {}
