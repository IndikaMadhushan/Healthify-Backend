package com.healthcare.personal_health_monitoring.dto;

import java.time.LocalDate;


public record NoteResponse(
        Long id,
        String description,
        LocalDate visitDate,
        String fileUrl
) {}
