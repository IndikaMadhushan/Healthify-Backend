package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;
import com.healthcare.personal_health_monitoring.repository.PatientHealthMetricRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.PatientHealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientHealthMetricServiceImpl
        implements PatientHealthMetricService {

    private final PatientRepository patientRepository;
    private final PatientHealthMetricRepository metricRepository;

    @Override
    public PatientHealthMetric addMetric(Long patientId,
                                         HealthMetricType metricType,
                                         Double value) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        PatientHealthMetric metric = new PatientHealthMetric();
        metric.setPatient(patient);
        metric.setMetricType(metricType);
        metric.setValue(value);
        metric.setRecordedAt(LocalDateTime.now());

        return metricRepository.save(metric);
    }

    @Override
    public List<PatientHealthMetric> getMetricsForGraph(
            Long patientId,
            HealthMetricType metricType) {

        return metricRepository
                .findByPatientIdAndMetricTypeOrderByRecordedAtAsc(
                        patientId, metricType
                );
    }
}

