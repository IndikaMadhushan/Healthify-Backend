//package com.healthcare.personal_health_monitoring.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "patient_health_metrics")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PatientHealthMetric {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "patient_id", nullable = false)
//    private Patient patient;
//
//    @Enumerated(EnumType.STRING)
//    private HealthMetricType metricType;
//
//    private Double value;
//
//    private LocalDateTime recordedAt;
//}

package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private HealthMetricType metricType;

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
    private PageType pageType;

    private int pageId;


}
