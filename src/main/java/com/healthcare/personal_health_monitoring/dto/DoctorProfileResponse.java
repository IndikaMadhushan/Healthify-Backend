package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.util.NameUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileResponse {

    private Long id;
    private String doctorId;
    private String firstName;
    private String secondName;
    private String lastName;
    private String email;
    private String nic;

    private String specialization;
    private String gender;
    private String hospital;
    private String licenseNumber;

    private String phone;
    private LocalDate dateOfBirth;
    private Integer age;

    private String photoUrl;


    public String getFullName() {
        return NameUtil.combine(firstName, secondName, lastName);
    }
}
