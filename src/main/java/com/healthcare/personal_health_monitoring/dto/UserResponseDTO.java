package com.healthcare.personal_health_monitoring.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
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

    private Set<String> diseases; // names
    private Set<String> allergies; // names

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
