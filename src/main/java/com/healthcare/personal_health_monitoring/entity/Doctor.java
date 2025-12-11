package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor extends User {

    private String specialization;

    private String licenseNumber;

    private LocalDate dateOfBirth;
    @Column(name = "age")
    private Integer age;

    private String photoUrl;

    private LocalDateTime joinedDate = LocalDateTime.now();

    public int getExperience() {
        return LocalDateTime.now().getYear() - joinedDate.getYear();
    }


}