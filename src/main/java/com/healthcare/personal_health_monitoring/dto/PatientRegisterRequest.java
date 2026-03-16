package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.util.NameUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String secondName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotBlank(message = "NIC is required")
    private String nic;

    @NotBlank(message = "Gender is required")
    private String gender;

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
