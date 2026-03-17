package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.ReviewRequest;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.Review;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.ReviewRepository;
import com.healthcare.personal_health_monitoring.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PatientRepository patientRepository;

    @Override
    public Review addReview(ReviewRequest request, long patientId) {

        // 🔥 get patient
        Patient patient = patientRepository.findByUserId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 🔥 create review
        Review review = new Review();
        review.setReview(request.getReview());

        // 🔥 SET FROM PATIENT TABLE
        review.setName(patient.getFullName());
        review.setImageUrl(patient.getPhotoUrl());
        review.setPatientId(patient.getPatientId());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}