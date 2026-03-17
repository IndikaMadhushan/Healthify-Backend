package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.SiteReview;
import com.healthcare.personal_health_monitoring.entity.SiteReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteReviewRepository extends JpaRepository<SiteReview, Long> {
    Optional<SiteReview> findTopByPatient_IdOrderByCreatedAtDesc(Long patientId);
    List<SiteReview> findByStatusOrderByCreatedAtDesc(SiteReviewStatus status);
    List<SiteReview> findByStatusOrderByReviewedAtDescCreatedAtDesc(SiteReviewStatus status);
}
