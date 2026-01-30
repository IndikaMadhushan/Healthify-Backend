package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "ui_medicine_reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UiMedicineReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;
    private String dosage;
    private String frequency;   // Daily, Weekly, As Needed
    private LocalTime time;
    private Integer duration;   // days
    private String notes;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}


