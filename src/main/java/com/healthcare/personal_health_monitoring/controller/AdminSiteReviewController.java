package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.AdminPendingSiteReviewResponse;
import com.healthcare.personal_health_monitoring.dto.SiteReviewResponse;
import com.healthcare.personal_health_monitoring.service.SiteReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/site-reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSiteReviewController {

    private final SiteReviewService siteReviewService;

    @GetMapping("/pending")
    public ResponseEntity<List<AdminPendingSiteReviewResponse>> getPendingReviews() {
        return ResponseEntity.ok(siteReviewService.getPendingReviews());
    }

    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<SiteReviewResponse> approveReview(
            @PathVariable Long reviewId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(siteReviewService.approveReview(reviewId, authentication.getName()));
    }

    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<SiteReviewResponse> rejectReview(
            @PathVariable Long reviewId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(siteReviewService.rejectReview(reviewId, authentication.getName()));
    }
}
