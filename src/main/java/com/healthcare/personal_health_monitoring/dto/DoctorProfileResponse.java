package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorProfileResponse {

    private String doctorId;
    private String fullName;
    private String email;
    private String nic;

    private String specialization;
    private String hospital;
    private String licenseNumber;

    private String phone;
    private Integer age;

    private String photoUrl;

}
