package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.AdminPendingSiteReviewResponse;
import com.healthcare.personal_health_monitoring.dto.CreateSiteReviewRequest;
import com.healthcare.personal_health_monitoring.dto.PublicSiteReviewResponse;
import com.healthcare.personal_health_monitoring.dto.SiteReviewEligibilityResponse;
import com.healthcare.personal_health_monitoring.dto.SiteReviewResponse;

import java.util.List;

public interface SiteReviewService {
    SiteReviewEligibilityResponse getEligibility(String patientEmail);
    SiteReviewResponse createReview(String patientEmail, CreateSiteReviewRequest request);
    SiteReviewResponse getLatestReview(String patientEmail);
    List<AdminPendingSiteReviewResponse> getPendingReviews();
    SiteReviewResponse approveReview(Long reviewId, String adminEmail);
    SiteReviewResponse rejectReview(Long reviewId, String adminEmail);
    List<PublicSiteReviewResponse> getApprovedReviews();
}
