package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Table(name= "consult_Page")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int consultId;

    @ManyToOne
    @JoinColumn(name = "DID", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "PID", nullable = false)
    private Patient patient;

    @Column(columnDefinition = "TEXT")
    private String consultReason;

    private LocalDate pagecreatedDate;
    private LocalTime pagecreatedTime;

    private LocalDate updatedDate;
    private LocalTime updatedTime;

    @PrePersist
    public void onCreate() {
        this.pagecreatedDate = LocalDate.now();
        this.pagecreatedTime = LocalTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedDate = LocalDate.now();
        this.updatedTime = LocalTime.now();
    }


    @Column(columnDefinition = "TEXT")
    private String consultExaming;

    @Column(columnDefinition = "TEXT")
    private String consultSuggestTest;

    @Column(columnDefinition = "TEXT")
    private String consultDoctorNote;

    @OneToMany(mappedBy = "consultPage",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Medication> medicaion;



}

