package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmergencyContactDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp="^\\+?[0-9\\-\\s]{6,20}$", message="Phone number must be valid")
    private String phoneNumber;

    @NotBlank(message = "Relationship is required")
    private String relationship;
}
