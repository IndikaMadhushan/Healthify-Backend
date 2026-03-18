package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicSiteReviewResponse {
    private Long id;
    private String patientName;
    private String patientPhotoUrl;
    private Integer rating;
    private String review;
    private LocalDateTime approvedAt;
}
