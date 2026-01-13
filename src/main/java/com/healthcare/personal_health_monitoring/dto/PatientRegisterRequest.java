package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRegisterRequest {
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "name is required")
    private String fullName;
    @Past(message = "dateOfBirth must be in the past")
    @NotNull(message = "Date of Birth is required")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$")
    private String phone;
    @NotBlank(message = "Nic is required")
    private String nic;
    @NotBlank(message = "Please select gender")
    private String gender;
}
