package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "patient_health_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientHealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "metric_type", nullable = false, length = 64)
    private HealthMetricType metricType;

    // BLOOD_SUGAR,
    // BLOOD_PRESSURE_SYSTOLIC,
    // BLOOD_PRESSURE_DIASTOLIC,
    // WEIGHT,
    // HEIGHT,
    // HEART_RATE,
    // TEMPERATURE,
    // CHOLESTEROL

    private Double value;

    private LocalDateTime recordedAt;

    @PrePersist
    public void onCreate() {
        this.recordedAt = LocalDateTime.now();
    }

    private LocalDateTime updateDateTime;

    @PreUpdate
    public void onUpdate() {
        this.updateDateTime = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "page_type")
    private PageType pageType;

    private int pageId;


}
