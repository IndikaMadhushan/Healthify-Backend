package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Collate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class PatientResponse {
    private Long id;
    private String fullName;
    private String email;
    private String nic;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private Double height;
    private Double weight;
    private String bloodType;
    private String postalCode;
    private String patientId;
    private String nationality;
    private String maritalStatus;
    private String occupation;
    private String district;
    private String address;
    private String phone;

    private EmergencyContactDTO primaryContact;
    private EmergencyContactDTO secondaryContact;

    private FamilyMemberDTO father;
    private FamilyMemberDTO mother;
    private List<FamilyMemberDTO> siblings;

    private Set<String> diseases; // names
    private Set<String> allergies; // names

    private LocalDateTime updatedAt;

    private boolean enabled;
    private String role;
    private LocalDate registrationDate;

    @Column(name = "profile_image")
    private String photoUrl;
}
