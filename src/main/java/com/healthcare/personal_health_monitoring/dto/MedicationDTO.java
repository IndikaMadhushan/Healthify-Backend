package com.healthcare.personal_health_monitoring.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDTO {



    private String drugName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instruction;
}
