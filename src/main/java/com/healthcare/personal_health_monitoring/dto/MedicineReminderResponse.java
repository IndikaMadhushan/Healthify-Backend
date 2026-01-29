package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class MedicineReminderResponse {
    private Long id;
    private String medicineName;
    private String reminderType;
    private LocalTime time;
    private boolean active;
    private String patientCode;
}
