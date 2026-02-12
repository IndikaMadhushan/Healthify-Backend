package com.healthcare.personal_health_monitoring.dto;


import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
public class ConsultPageFullDTO {

    // Consult
    private int consultId;
    private String consultReason;
    private String consultExaming;
    private String consultSuggestTest;
    private String consultDoctorNote;

    private LocalDate createdDate;
    private LocalTime createdTime;
    private LocalDate updatedDate;
    private LocalTime updatedTime;

    // Patient
    private String patientName;
    private String patientGender;
    private Integer patientAge;

    // Doctor
    private String doctorName;
    private String doctorSpecialization;
    private String slmc;

    // Medication
    private List<MedicationDTO> medications;

    // Health metrics
    private Map<HealthMetricType, Double> healthMetrics;
}