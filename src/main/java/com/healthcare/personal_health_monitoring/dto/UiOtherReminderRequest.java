package com.healthcare.personal_health_monitoring.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UiOtherReminderRequest {
    private String title;
    private String category;
    private LocalDate reminderDate;
    private LocalTime time;
    private String description;
    private String icon;
}

