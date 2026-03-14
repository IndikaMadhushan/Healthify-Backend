package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorRegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

//    @NotBlank(message = "Phone number is required")
//    @Pattern(regexp = "^\\d{10}$")
//    private String phone;
    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "NIC is required")
    private String nic;

    @NotBlank(message = "Hospital is required")
    private String hospital;
}
