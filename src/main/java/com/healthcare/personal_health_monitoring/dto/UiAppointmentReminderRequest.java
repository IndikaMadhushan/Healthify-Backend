package com.healthcare.personal_health_monitoring.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UiAppointmentReminderRequest {
    private String title;
    private LocalDate appointmentDate;
    private LocalTime time;
    private String location;
    private String doctor;
    private String reason;
}

