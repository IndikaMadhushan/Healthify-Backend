package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String fullName;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String role; // PATIENT, DOCTOR, ADMIN

    @Column(nullable=true)
    private String nic;

    @Column(nullable=true)
    private String postalCode;


    @Column(nullable=true)
    private String phone;

    @Column(nullable=true)
    private String district;

    @Column(nullable=true)
    private String province;

    @Column(nullable=true)
    private String country;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
