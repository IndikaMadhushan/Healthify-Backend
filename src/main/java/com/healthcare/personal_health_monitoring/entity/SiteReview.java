package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "site_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "patient_name_snapshot", nullable = false)
    private String patientNameSnapshot;

    @Column(name = "patient_email_snapshot", nullable = false)
    private String patientEmailSnapshot;

    @Column(name = "patient_photo_url_snapshot", length = 1000)
    private String patientPhotoUrlSnapshot;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "review_text", nullable = false, length = 300)
    private String reviewText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SiteReviewStatus status = SiteReviewStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_admin_id")
    private Admin reviewedByAdmin;
}
