package com.healthcare.personal_health_monitoring.entity;

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

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserRole role; // PATIENT, DOCTOR, ADMIN
    private boolean enabled = true;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;
    //otp verification part
    @Column(name = "email_otp")
    private String emailOtp;
    @Column(name = "otp_generated_at", nullable = true)
    private LocalDateTime otpGeneratedAt;

    @Column(nullable=false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
