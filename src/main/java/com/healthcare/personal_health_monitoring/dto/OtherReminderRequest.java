package com.healthcare.personal_health_monitoring.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class OtherReminderRequest {
    // DAILY, WEEKLY, SPECIFIC_DATE
    private String reminderType;
    private LocalDate specificDate;
    private LocalTime time;
    private String daysOfWeek;
    private String note;
}
