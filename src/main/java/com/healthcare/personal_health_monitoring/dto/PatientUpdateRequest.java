package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.util.NameUtil;
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

    private String firstName;
    private String secondName;
    private String lastName;

    @Email(message = "Email must be a valid email address")
    private String email;

    private String nic;

    @Past(message = "Date of birth must be in the past")
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

    private Set<Long> diseaseIds;
    private Set<Long> allergyIds;

    private Set<Long> surgeryIds;
    private Set<Long> noteIds;

    private String nationality;

    public String getFullName() {
        return NameUtil.combine(firstName, secondName, lastName);
    }

    public void setFullName(String fullName) {
        NameUtil.NameParts parts = NameUtil.split(fullName);
        this.firstName = parts.firstName();
        this.secondName = parts.secondName();
        this.lastName = parts.lastName();
    }
}
