package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient_diseases")
public class PatientDisease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="patient_id", nullable=false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name="disease_id", nullable=false)
    private Disease disease;
}
