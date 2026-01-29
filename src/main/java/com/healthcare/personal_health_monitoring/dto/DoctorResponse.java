package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {

    // internal DB id (mostly for admin use)
    private Long id;

    // GENERATED doctor code (what doctors/admins actually use)
    private String doctorId;

    private String fullName;
    private String email;

    private String nic;
    private String phone;

    private String specialization;
    private String licenseNumber;
    private String verificationDocUrl;


    private String gender;
    private String hospital;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Integer age;
    private Integer experience;

    private String photoUrl;

    private LocalDateTime joinedDate;

    private Boolean enabled;
    private String role;
}
