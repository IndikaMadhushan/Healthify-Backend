package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient_emergency_contacts")
public class PatientEmergencyContact {

    @Id
    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "primary_contact_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "primary_contact_phone")),
            @AttributeOverride(name = "relationship", column = @Column(name = "primary_contact_relationship"))
    })
    private EmergencyContact primaryContact;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "secondary_contact_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "secondary_contact_phone")),
            @AttributeOverride(name = "relationship", column = @Column(name = "secondary_contact_relationship"))
    })
    private EmergencyContact secondaryContact;
}
