package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.util.NameUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorUpdateRequest {
//    @NotBlank(message = "First name is required")
//    private String firstName;
//
//    private String secondName;
//
//    @NotBlank(message = "Last name is required")
//    private String lastName;
private String fullName;
    private String phone;

    @NotBlank(message = "Hospital is required")
    private String hospital;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private LocalDate dateOfBirth;

//    public String getFullName() {
//        return NameUtil.combine(firstName, secondName, lastName);
//    }
//
//    public void setFullName(String fullName) {
//        NameUtil.NameParts parts = NameUtil.split(fullName);
//        this.firstName = parts.firstName();
//        this.secondName = parts.secondName();
//        this.lastName = parts.lastName();
//    }
}
