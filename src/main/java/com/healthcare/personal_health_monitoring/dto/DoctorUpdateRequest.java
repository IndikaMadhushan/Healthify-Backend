package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorUpdateRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phone;

    @NotBlank(message = "Hospital is required")
    private String hospital;

    @NotBlank(message = "Specialization is required")
    private String specialization;
}
