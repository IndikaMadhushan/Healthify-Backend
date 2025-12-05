package com.healthcare.personal_health_monitoring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password; // plaintext here — will be encoded in service

    private String phone;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private Double height;
    private Double weight;

    private String address;
    private String city;
    private String district;
    private String province;
    private String country;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;

    // Optional lists of existing disease/allergy IDs to add during registration
    private Set<Long> diseaseIds;
    private Set<Long> allergyIds;
}
