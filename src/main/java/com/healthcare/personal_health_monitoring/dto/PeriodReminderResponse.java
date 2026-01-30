package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PeriodReminderResponse {

    private Long id;
    private LocalDate lastPeriodDate;
    private Integer cycleLength;
    private LocalDate nextPeriodDate;
    private boolean active;
}
