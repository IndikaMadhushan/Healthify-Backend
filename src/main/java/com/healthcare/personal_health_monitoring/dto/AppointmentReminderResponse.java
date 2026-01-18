package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AppointmentReminderResponse {

    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String hospital;
    private String doctorName;
    private String note;
    private boolean completed;

}
