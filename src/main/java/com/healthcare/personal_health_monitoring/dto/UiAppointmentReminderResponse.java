package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class UiAppointmentReminderResponse {
    private Long id;
    private String title;
    private LocalDate appointmentDate;
    private LocalTime time;
    private String location;
    private String doctor;
    private String reason;
}
