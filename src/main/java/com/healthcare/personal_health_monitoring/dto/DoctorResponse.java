package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Integer age;
    private Integer experience;

    private String photoUrl;

    private LocalDateTime joinedDate;
}
