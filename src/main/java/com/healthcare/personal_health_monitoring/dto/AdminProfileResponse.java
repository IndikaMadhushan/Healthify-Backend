package com.healthcare.personal_health_monitoring.dto;

import java.time.LocalDateTime;

public record AdminProfileResponse(
        Long id,
        String email,
        String role,
        boolean enabled,
        boolean emailVerified,
        LocalDateTime createdAt
) {}
