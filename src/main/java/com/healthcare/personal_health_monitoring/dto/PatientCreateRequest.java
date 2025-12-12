package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class PatientCreateRequest {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    private String nic;

    @Past(message = "dateOfBirth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String gender;
    private Double height; // cm
    private Double weight; // kg
    private String bloodType;
    private String postalCode;

    @Valid
    private EmergencyContactDTO primaryContact;

    @Valid
    private EmergencyContactDTO secondaryContact;

    @Valid
    private FamilyMemberDTO father;

    @Valid
    private FamilyMemberDTO mother;

    @Valid
    private List<FamilyMemberDTO> siblings;

    // Link pre-populated disease/allergy ids
    private Set<Long> diseaseIds;
    private Set<Long> allergyIds;
}
