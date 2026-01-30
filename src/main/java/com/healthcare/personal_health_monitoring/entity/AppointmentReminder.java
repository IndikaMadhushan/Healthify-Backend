//package com.healthcare.personal_health_monitoring.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Entity
//@Data
//@Table(name = "appointment_reminders")
//public class AppointmentReminder {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private LocalDate appointmentDate;
//
//    private LocalTime appointmentTime;
//
//    private String hospital;
//
//    private String doctorName;
//
//    private String note;
//
//    private boolean completed = false;
//
//    @ManyToOne
//    @JoinColumn(name = "patient_id")
//    private Patient patient;
//
//}
