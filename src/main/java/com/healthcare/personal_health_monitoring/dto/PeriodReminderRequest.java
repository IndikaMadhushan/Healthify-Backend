package com.healthcare.personal_health_monitoring.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PeriodReminderRequest {
    private LocalDate lastPeriodDate;
    private Integer cycleLength;
    private Integer periodDuration;
    private String notes;
}
