package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.CreateSiteReviewRequest;
import com.healthcare.personal_health_monitoring.dto.SiteReviewEligibilityResponse;
import com.healthcare.personal_health_monitoring.dto.SiteReviewResponse;
import com.healthcare.personal_health_monitoring.service.SiteReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients/me/site-review")
@RequiredArgsConstructor
public class PatientSiteReviewController {

    private final SiteReviewService siteReviewService;

    @GetMapping("/eligibility")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<SiteReviewEligibilityResponse> getEligibility(Authentication authentication) {
        return ResponseEntity.ok(siteReviewService.getEligibility(authentication.getName()));
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<SiteReviewResponse> createReview(
            @Valid @RequestBody CreateSiteReviewRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(siteReviewService.createReview(authentication.getName(), request));
    }

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<SiteReviewResponse> getLatestReview(Authentication authentication) {
        SiteReviewResponse response = siteReviewService.getLatestReview(authentication.getName());
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.noContent().build();
    }
}
