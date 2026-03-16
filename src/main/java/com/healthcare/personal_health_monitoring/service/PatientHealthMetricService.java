package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;

import java.util.List;
import java.util.Optional;

public interface PatientHealthMetricService {

    PatientHealthMetric addMetric(
            Long patientId,
            HealthMetricType metricType,
            Double value
    );

    List<PatientHealthMetric> getMetricsForGraph(
            Long patientId,
            HealthMetricType metricType
    );

    Optional<PatientHealthMetric> getLatestMetric(
            Long patientId,
            HealthMetricType metricType
    );
}
