package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Doctor doctor;

    private LocalDate visitDate;

    @Column(length = 2000)
    private String description;

    // Can be prescription OR report (PDF / image)
    // cloud/local storage URL
    private String fileUrl;
}
