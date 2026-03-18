package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPendingSiteReviewResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhotoUrl;
    private Integer rating;
    private String review;
    private String status;
    private LocalDateTime createdAt;
}
