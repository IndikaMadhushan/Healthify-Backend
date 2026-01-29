package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportType;
    private LocalDate reportDate;

    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
