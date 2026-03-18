package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.ReviewRequest;
import com.healthcare.personal_health_monitoring.entity.Review;
import com.healthcare.personal_health_monitoring.repository.ReviewRepository;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        long patientId = userDetails.getUser().getId();

        return reviewService.addReview(request, patientId);
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }
}