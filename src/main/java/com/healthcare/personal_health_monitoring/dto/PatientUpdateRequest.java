package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class PatientUpdateRequest {

    // All fields optional — only provided fields will be updated
    private String fullName;

    @Email
    private String email;

    private String nic;

    @Past(message = "dateOfBirth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String gender;
    private Double height;
    private Double weight;
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

    // Replace lists when provided
    private Set<Long> diseaseIds;
    private Set<Long> allergyIds;

    // optional lists to link existing notes/surgeries if needed later
    private Set<Long> surgeryIds;
    private Set<Long> noteIds;
}
