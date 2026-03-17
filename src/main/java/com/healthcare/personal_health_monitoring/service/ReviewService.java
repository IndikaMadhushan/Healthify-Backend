package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.ReviewRequest;
import com.healthcare.personal_health_monitoring.entity.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(ReviewRequest request, long patientId);

    List<Review> getAllReviews();
}
