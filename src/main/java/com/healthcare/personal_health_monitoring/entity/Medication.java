package com.healthcare.personal_health_monitoring.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "medication")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "clinic_page_id")
    private ClinicPage clinicPage;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private ConsultPage consultPage;

    private String drugName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instruction;


}