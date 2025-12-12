package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmergencyContactDTO {
    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp="^\\+?[0-9\\-\\s]{6,20}$", message="invalid phone")
    private String phoneNumber;

    @NotBlank
    private String relationship;
}
