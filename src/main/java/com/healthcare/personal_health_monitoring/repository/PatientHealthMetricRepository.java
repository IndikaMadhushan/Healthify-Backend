package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientHealthMetricRepository
        extends JpaRepository<PatientHealthMetric, Long> {

    List<PatientHealthMetric> findByPatientIdAndMetricTypeOrderByRecordedAtAsc(
            Long patientId,
            HealthMetricType metricType
    );

    List<PatientHealthMetric> findByPatientId(Long patientId);
}

