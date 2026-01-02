package com.healthcare.personal_health_monitoring.util;

import com.healthcare.personal_health_monitoring.dto.PatientHealthMetricResponse;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;

public class PatientHealthMetricMapper {

    public static PatientHealthMetricResponse toResponse(PatientHealthMetric metric) {
        return new PatientHealthMetricResponse(
                metric.getId(),
                metric.getPatient().getId(),
                metric.getMetricType(),
                metric.getValue(),
                metric.getRecordedAt()
        );
    }
}
