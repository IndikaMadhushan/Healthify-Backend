package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ui_other_reminders")
@Data
public class UiOtherReminder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private LocalDate reminderDate;
    private LocalTime time;
    private String description;
    private String icon;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}



