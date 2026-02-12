package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicBookViewDTO {

    private int id;
    // Doctor data
    private String doctorFullName;
    private String specialization;
    private String licenseNumber;

    // Clinic book data
    private String visitReason;
    private String accessControl;

    private String updatedDoctor;
    private String updatedTime; // String to handle "Not updated yet"
}