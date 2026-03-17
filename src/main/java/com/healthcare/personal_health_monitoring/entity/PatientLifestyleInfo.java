package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient_lifestyle_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientLifestyleInfo {

    @Id
    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String smokingStatus;
    private String smokingFrequency;
    private String alcoholStatus;
    private String alcoholFrequency;
    private String drugUseStatus;
    private String drugUseFrequency;
    private String stressLevel;

    @Column(length = 1000)
    private String foodAllergies;

    @Column(length = 1000)
    private String drugAllergies;
}
