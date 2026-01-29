package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "other_reminders")
@AllArgsConstructor
@NoArgsConstructor
public class OtherReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DAILY, WEEKLY, SPECIFIC_DATE, OTHER
    private String reminderType;
    private LocalDate specificDate;
    private LocalTime time;

    // "MON,TUE,WED"
    private String daysOfWeek;

    private String note;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
