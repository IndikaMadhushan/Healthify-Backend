package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientHealthMetricResponse {

    private Long id;
    private Long patientId;
    private HealthMetricType metricType;
    private Double value;
    private LocalDateTime recordedAt;
}
