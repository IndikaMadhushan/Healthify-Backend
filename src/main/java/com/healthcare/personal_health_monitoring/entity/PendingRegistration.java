package com.healthcare.personal_health_monitoring.entity;

import com.healthcare.personal_health_monitoring.entity.converter.EncryptedStringConverter;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import jakarta.persistence.Convert;
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

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = false, unique = true, length = 512)
    private String email;

    @Column(name = "email_hash", unique = true, length = 64)
    private String emailHash;

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

        @Convert(converter = EncryptedStringConverter.class)
        @Column(name = "full_name", nullable = false, length = 512)
        private String fullName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Convert(converter = EncryptedStringConverter.class)
    private String phone;

    @Convert(converter = EncryptedStringConverter.class)
    private String nic;

    @Column(name = "nic_hash", length = 64)
    private String nicHash;

    @Convert(converter = EncryptedStringConverter.class)
    private String gender;

    private String specialization;
    private String hospital;
    private String licenseNumber;
    @Column(length = 1000)
    private String verificationDocUrl;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = false, length = 512)
    private String emailOtp;

    @Column(nullable = false)
    private LocalDateTime otpGeneratedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    public String getFullName() {
        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }
        return name != null ? name.getFullName() : null;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public void setEmail(String email) {
        this.email = email;
        this.emailHash = SensitiveDataSupport.blindIndex(email);
    }

    public void setNic(String nic) {
        this.nic = nic;
        this.nicHash = SensitiveDataSupport.blindIndex(nic);
    }
}
