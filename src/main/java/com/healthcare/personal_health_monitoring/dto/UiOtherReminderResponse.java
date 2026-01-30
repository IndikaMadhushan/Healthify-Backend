package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class UiOtherReminderResponse {
    private Long id;
    private String title;
    private String category;
    private LocalDate reminderDate;
    private LocalTime time;
    private String description;
    private String icon;
}

