package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByPatientId(String patientId);
}