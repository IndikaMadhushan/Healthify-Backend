package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pending_registrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "secondName", column = @Column(name = "second_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
    })
    private PersonName name = new PersonName();

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private String phone;
    private String nic;
    private String gender;

    private String specialization;
    private String hospital;
    private String licenseNumber;
    @Column(length = 1000)
    private String verificationDocUrl;

    @Column(nullable = false)
    private String emailOtp;

    @Column(nullable = false)
    private LocalDateTime otpGeneratedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    public String getFullName() {
        return name != null ? name.getFullName() : null;
    }

    public void setFullName(String fullName) {
        if (name == null) {
            name = new PersonName();
        }
        name.setFullName(fullName);
    }

    @Transient
    public String getFirstName() {
        return name != null ? name.getFirstName() : null;
    }

    public void setFirstName(String firstName) {
        if (name == null) {
            name = new PersonName();
        }
        name.setFirstName(firstName);
    }

    @Transient
    public String getSecondName() {
        return name != null ? name.getSecondName() : null;
    }

    public void setSecondName(String secondName) {
        if (name == null) {
            name = new PersonName();
        }
        name.setSecondName(secondName);
    }

    @Transient
    public String getLastName() {
        return name != null ? name.getLastName() : null;
    }

    public void setLastName(String lastName) {
        if (name == null) {
            name = new PersonName();
        }
        name.setLastName(lastName);
    }
}
