package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ui_appointment_reminders")
@Data
public class UiAppointmentReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate appointmentDate;
    private LocalTime time;
    private String location;
    private String doctor;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private boolean completed;
}


