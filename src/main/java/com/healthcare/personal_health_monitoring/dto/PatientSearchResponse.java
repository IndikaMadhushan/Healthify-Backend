package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientSearchResponse {
    private Long id;
    private String patientId;
    private String fullName;
    private String nic;
    private String gender;
}
