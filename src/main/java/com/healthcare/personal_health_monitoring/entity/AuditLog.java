package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //who performed the action
    private String actorEmail;
    private String actorRole;

    // what happens
    //VIEW_REPORTS, DOWNLOAD_REPORTS
    private String action;

    // on which patient
    private Long patientId;

    // optional context
    private String description;

    private LocalDateTime timestamp;
}


