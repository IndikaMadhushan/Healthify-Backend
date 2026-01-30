package com.healthcare.personal_health_monitoring.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class UiMedicineReminderRequest {
    private String medicineName;
    private String dosage;
    private String frequency;
    private LocalTime time;
    private Integer duration;
    private String notes;
}

