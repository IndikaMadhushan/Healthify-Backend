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
@Table(name = "patient_medical_info_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicalInfoProfile {

    @Id
    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(length = 1000)
    private String otherChronic;

    @Column(length = 1000)
    private String cancerChronic;

    @Column(length = 1000)
    private String otherVaccine;
}
