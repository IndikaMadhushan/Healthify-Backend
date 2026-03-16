package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_report_files")
@Data @AllArgsConstructor @NoArgsConstructor
public class LabReportFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // null = root level, non-null = inside a folder
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private LabReportFolder folder;

    @Column(nullable = false, length = 30)
    private String title;

    // original filename e.g. "blood_test.pdf"
    @Column(nullable = false)
    private String originalName;

    // MIME type e.g. "application/pdf", "image/jpeg"
    @Column(nullable = false)
    private String fileType;

    // URL returned by FileUploadService (Cloudinary/local path)
    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
