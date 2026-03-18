package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.PublicSiteReviewResponse;
import com.healthcare.personal_health_monitoring.service.SiteReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/site-reviews")
@RequiredArgsConstructor
public class PublicSiteReviewController {

    private final SiteReviewService siteReviewService;

    @GetMapping("/public")
    public ResponseEntity<List<PublicSiteReviewResponse>> getApprovedReviews() {
        return ResponseEntity.ok(siteReviewService.getApprovedReviews());
    }
}
