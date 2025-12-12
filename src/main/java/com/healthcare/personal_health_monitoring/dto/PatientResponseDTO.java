package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class PatientResponseDTO {
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

    private EmergencyContactDTO primaryContact;
    private EmergencyContactDTO secondaryContact;

    private FamilyMemberDTO father;
    private FamilyMemberDTO mother;
    private List<FamilyMemberDTO> siblings;

    private Set<String> diseases;
    private Set<String> allergies;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
