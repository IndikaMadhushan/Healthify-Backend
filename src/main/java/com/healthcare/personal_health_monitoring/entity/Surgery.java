package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name="surgeries")
@Data @AllArgsConstructor @NoArgsConstructor
public class Surgery {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    private String description;

    private String hospital;

    private String sugeryComplication;

    private LocalDate surgeryDate;
}
