package com.healthcare.personal_health_monitoring.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ConsultPageDTO {


    private String consultReason;
    private List<HealthMetricRequestSetDTO> patientHealthMetricRequestDTOS;
    private String consultExaming;
    private String consultSuggestTest;
    private String consultDoctorNote;
    private List<MedicationDTO> medicationDTOS;
}
