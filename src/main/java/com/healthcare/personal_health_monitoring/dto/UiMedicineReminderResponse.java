package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class UiMedicineReminderResponse {
    private Long id;
    private String medicineName;
    private String dosage;
    private String frequency;
    private LocalTime time;
    private Integer duration;
    private String notes;
}
