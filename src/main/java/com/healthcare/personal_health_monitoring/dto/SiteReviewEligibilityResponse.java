package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteReviewEligibilityResponse {
    private boolean canPrompt;
    private String reason;
    private LocalDateTime registrationDate;
    private LocalDateTime eligibleFrom;
    private String existingReviewStatus;
}
