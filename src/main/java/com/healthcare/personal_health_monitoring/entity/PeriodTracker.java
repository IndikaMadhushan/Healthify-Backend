package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "period_tracker")
@Data
public class PeriodTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate lastPeriodDate;
    private Integer cycleLength;
    private Integer periodDuration;
    private String notes;
    private boolean active;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}


