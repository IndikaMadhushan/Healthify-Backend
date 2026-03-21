package com.healthcare.personal_health_monitoring.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;
import com.healthcare.personal_health_monitoring.repository.PatientHealthMetricRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientContextService {

    private final PatientRepository patientRepository;
    private final PatientHealthMetricRepository metricRepository;

    public String getPatientHealthContext(Long patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isEmpty()) {
                return "";
            }

            Patient patient = patientOpt.get();
            StringBuilder context = new StringBuilder();

            context.append("Patient: ");
            context.append(patient.getAge()).append(" years old");

            if (patient.getBloodType() != null) {
                context.append(", Blood Type: ").append(patient.getBloodType());
            }
            if (patient.getWeight() != null) {
                context.append(", Weight: ").append(patient.getWeight()).append(" kg");
            }
            if (patient.getHeight() != null) {
                context.append(", Height: ").append(patient.getHeight()).append(" cm");
            }

            List<PatientHealthMetric> metrics = metricRepository.findByPatientId(patientId);
            if (!metrics.isEmpty()) {
                context.append(". Recent metrics: ");
                String metricsStr = metrics.stream()
                        .map(m -> m.getMetricType() + "=" + m.getValue())
                        .limit(5)
                        .collect(Collectors.joining(", "));
                context.append(metricsStr);
            }

            return context.toString();
        } catch (Exception e) {
            log.error("Error retrieving patient context", e);
            return "";
        }
    }

    public String getPatientQuickSummary(Long patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isEmpty()) {
                return "Patient not found";
            }

            Patient patient = patientOpt.get();
            return "Age: " + patient.getAge() + " years, " +
                    "Blood Type: " + (patient.getBloodType() != null ? patient.getBloodType() : "Unknown");
        } catch (Exception e) {
            log.error("Error retrieving patient summary", e);
            return "Unable to retrieve patient data";
        }
    }
}
