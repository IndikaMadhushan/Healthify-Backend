package com.healthcare.personal_health_monitoring.entity;


import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name= "clinic_book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "PID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "DID", nullable = false)
    private Doctor doctor;



    @Column(nullable=false)
    private String visit_reason;



    private LocalDate createdDate;
    private LocalTime createdTime;

    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDate.now();
        this.createdTime = LocalTime.now();
    }







}
