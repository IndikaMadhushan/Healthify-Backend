package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HealthMetricRequestSetDTO {
    private Map<HealthMetricType, Double> metrics;
}

