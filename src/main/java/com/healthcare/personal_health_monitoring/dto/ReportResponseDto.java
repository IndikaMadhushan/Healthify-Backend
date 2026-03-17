package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private Long id;
    private Long patientId;
    private String reportType;
    private LocalDate reportDate;
    private String fileUrl;
}
