package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "OTP is required") String otp,
        @NotBlank(message = "New password is required") String newPassword
) {}
