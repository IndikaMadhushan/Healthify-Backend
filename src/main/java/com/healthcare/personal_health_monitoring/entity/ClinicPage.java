package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Table(name= "clinic_Page")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clinicPageId;

    @ManyToOne
    @JoinColumn(name = "clinic_book_id", nullable = false)
    private ClinicBook clinicBook;

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
    private String subReason;

    private String clinicExaming;

    private String clinicSuggestTest;

    private String clinicDoctorNote;

    private Date nextClinic;

    @OneToMany(mappedBy = "clinicPage",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Medication> medication;

    private String UpdatedDoctor;


    @Column(nullable = false)
    private boolean patientApprovedForEdit = false;

    private LocalDateTime patientApprovedTime;


}
