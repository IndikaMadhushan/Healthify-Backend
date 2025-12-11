package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="notes")
@Data @AllArgsConstructor @NoArgsConstructor
public class Note {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private LocalDate date;

    private String description;

    private String prescriptionUrl; // cloud URL
}
