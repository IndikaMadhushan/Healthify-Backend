package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate lastPeriodDate;
    private Integer cycleLength = 28;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public LocalDate getNextPeriodDate() {
        return lastPeriodDate.plusDays(cycleLength);
    }
}
