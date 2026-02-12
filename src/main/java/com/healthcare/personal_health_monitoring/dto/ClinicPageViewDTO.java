package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClinicPageViewDTO {

    private Long clinicPageId;

    private String subReason;
    private String clinicExaming;
    private String clinicSuggestTest;
    private String clinicDoctorNote;

    private LocalDateTime nextClinic;

    private String createdDoctor;
    private String slmc;

    private String patientName;
    private Integer patientAge;
    private String patientGender;

    private List<MedicationDTO> medication;

    private HealthMetricRequestSetDTO healthMetricRequestSetDTO;
}