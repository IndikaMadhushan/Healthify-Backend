package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="patient_allergies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAllergy {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Allergy allergy;

    private LocalDate onsetDate;
}

