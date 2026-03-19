package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.ContactUsRequest;
import com.healthcare.personal_health_monitoring.dto.ContactUsResponse;
import com.healthcare.personal_health_monitoring.exception.TooManyContactRequestsException;
import com.healthcare.personal_health_monitoring.service.ContactUsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/contact-us")
@RequiredArgsConstructor
public class ContactUsController {

    private static final Logger log = LoggerFactory.getLogger(ContactUsController.class);

    private final ContactUsService contactUsService;

    @PostMapping
    public ResponseEntity<ContactUsResponse> submitContactMessage(
            @RequestBody(required = false) ContactUsRequest request,
            HttpServletRequest httpServletRequest
    ) {
        ContactUsRequest normalizedRequest = request == null
                ? new ContactUsRequest(null, null, null, null, null).normalized()
                : request.normalized();

        Map<String, String> errors = contactUsService.validate(normalizedRequest);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(ContactUsResponse.validationFailure(errors));
        }

        try {
            contactUsService.submit(
                    normalizedRequest,
                    extractClientIp(httpServletRequest),
                    sanitizeSingleLine(httpServletRequest.getHeader("User-Agent"))
            );
            return ResponseEntity.ok(ContactUsResponse.success("Message sent successfully"));
        } catch (TooManyContactRequestsException ex) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ContactUsResponse.failure(ex.getMessage()));
        } catch (Exception ex) {
            log.error("Failed to process contact request from {}", normalizedRequest.email(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ContactUsResponse.failure("Failed to send contact message"));
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwardedFor = sanitizeSingleLine(request.getHeader("X-Forwarded-For"));
        if (forwardedFor != null) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = sanitizeSingleLine(request.getHeader("X-Real-IP"));
        if (realIp != null) {
            return realIp;
        }

        return sanitizeSingleLine(request.getRemoteAddr());
    }

    private String sanitizeSingleLine(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed.replaceAll("[\\r\\n]+", " ");
    }
}
