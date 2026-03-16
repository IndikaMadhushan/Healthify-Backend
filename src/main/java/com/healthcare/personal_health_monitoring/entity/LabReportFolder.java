package com.healthcare.personal_health_monitoring.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_report_folders")
@Data @AllArgsConstructor @NoArgsConstructor

public class LabReportFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(nullable = false, length = 30)
    private String name;

    // null = root level, non-null = nested inside another folder
    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private LabReportFolder parentFolder;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
