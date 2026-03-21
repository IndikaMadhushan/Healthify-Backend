package com.healthcare.personal_health_monitoring.entity;

import com.healthcare.personal_health_monitoring.entity.converter.EncryptedStringConverter;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import jakarta.persistence.Convert;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable=false, unique=true, length = 512)
    private String email;

    @Column(name = "email_hash", unique = true, length = 64)
    private String emailHash;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserRole role; // PATIENT, DOCTOR, ADMIN
    private boolean enabled = true;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;
    //otp verification part
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "email_otp", length = 512)
    private String emailOtp;
    @Column(name = "otp_generated_at", nullable = true)
    private LocalDateTime otpGeneratedAt;

    @Column(nullable=false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public void setEmail(String email) {
        this.email = email;
        this.emailHash = SensitiveDataSupport.blindIndex(email);
    }
}
