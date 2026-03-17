package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.AdminPendingSiteReviewResponse;
import com.healthcare.personal_health_monitoring.dto.CreateSiteReviewRequest;
import com.healthcare.personal_health_monitoring.dto.PublicSiteReviewResponse;
import com.healthcare.personal_health_monitoring.dto.SiteReviewEligibilityResponse;
import com.healthcare.personal_health_monitoring.dto.SiteReviewResponse;
import com.healthcare.personal_health_monitoring.entity.Admin;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.SiteReview;
import com.healthcare.personal_health_monitoring.entity.SiteReviewStatus;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.AdminRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.SiteReviewRepository;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.service.SiteReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteReviewServiceImpl implements SiteReviewService {

    private static final long ELIGIBILITY_DELAY_DAYS = 2;

    private final SiteReviewRepository siteReviewRepository;
    private final PatientRepository patientRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public SiteReviewEligibilityResponse getEligibility(String patientEmail) {
        Patient patient = patientRepository.findByUserEmail(patientEmail).orElse(null);
        if (patient == null || patient.getUser() == null || patient.getUser().getCreatedAt() == null) {
            return new SiteReviewEligibilityResponse(false, "PROFILE_NOT_FOUND", null, null, null);
        }

        LocalDateTime registrationDate = patient.getUser().getCreatedAt();
        LocalDateTime eligibleFrom = registrationDate.plusDays(ELIGIBILITY_DELAY_DAYS);
        SiteReview latestReview = findLatestReview(patient.getId());
        String existingReviewStatus = latestReview != null ? latestReview.getStatus().name() : null;

        if (LocalDateTime.now().isBefore(eligibleFrom)) {
            return new SiteReviewEligibilityResponse(false, "TOO_EARLY", registrationDate, eligibleFrom, existingReviewStatus);
        }
        if (latestReview != null && latestReview.getStatus() == SiteReviewStatus.PENDING) {
            return new SiteReviewEligibilityResponse(false, "ALREADY_PENDING", registrationDate, eligibleFrom, existingReviewStatus);
        }
        if (latestReview != null && latestReview.getStatus() == SiteReviewStatus.APPROVED) {
            return new SiteReviewEligibilityResponse(false, "ALREADY_APPROVED", registrationDate, eligibleFrom, existingReviewStatus);
        }

        return new SiteReviewEligibilityResponse(true, "CAN_SUBMIT", registrationDate, eligibleFrom, existingReviewStatus);
    }

    @Override
    public SiteReviewResponse createReview(String patientEmail, CreateSiteReviewRequest request) {
        Patient patient = getPatient(patientEmail);
        SiteReviewEligibilityResponse eligibility = getEligibility(patientEmail);
        if (!eligibility.isCanPrompt()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Site review cannot be submitted: " + eligibility.getReason());
        }

        String reviewText = trimToNull(request.getReview());
        if (reviewText == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "review is required");
        }
        if (reviewText.length() > 300) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "review must be at most 300 characters");
        }

        SiteReview siteReview = new SiteReview();
        siteReview.setPatient(patient);
        siteReview.setPatientNameSnapshot(resolvePatientName(patient));
        siteReview.setPatientEmailSnapshot(resolvePatientEmail(patient));
        siteReview.setPatientPhotoUrlSnapshot(trimToNull(patient.getPhotoUrl()));
        siteReview.setRating(request.getRating());
        siteReview.setReviewText(reviewText);
        siteReview.setStatus(SiteReviewStatus.PENDING);
        siteReview.setCreatedAt(LocalDateTime.now());
        siteReview.setReviewedAt(null);
        siteReview.setReviewedByAdmin(null);

        return toSiteReviewResponse(siteReviewRepository.save(siteReview));
    }

    @Override
    @Transactional(readOnly = true)
    public SiteReviewResponse getLatestReview(String patientEmail) {
        Patient patient = patientRepository.findByUserEmail(patientEmail).orElse(null);
        if (patient == null) {
            return null;
        }

        SiteReview latestReview = findLatestReview(patient.getId());
        return latestReview != null ? toSiteReviewResponse(latestReview) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminPendingSiteReviewResponse> getPendingReviews() {
        return siteReviewRepository.findByStatusOrderByCreatedAtDesc(SiteReviewStatus.PENDING).stream()
                .map(this::toAdminPendingResponse)
                .toList();
    }

    @Override
    public SiteReviewResponse approveReview(Long reviewId, String adminEmail) {
        return moderateReview(reviewId, adminEmail, SiteReviewStatus.APPROVED);
    }

    @Override
    public SiteReviewResponse rejectReview(Long reviewId, String adminEmail) {
        return moderateReview(reviewId, adminEmail, SiteReviewStatus.REJECTED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicSiteReviewResponse> getApprovedReviews() {
        return siteReviewRepository.findByStatusOrderByReviewedAtDescCreatedAtDesc(SiteReviewStatus.APPROVED).stream()
                .map(this::toPublicResponse)
                .toList();
    }

    private SiteReviewResponse moderateReview(Long reviewId, String adminEmail, SiteReviewStatus targetStatus) {
        SiteReview siteReview = siteReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site review not found"));

        if (siteReview.getStatus() != SiteReviewStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending site reviews can be moderated");
        }

        Admin admin = resolveAdminProfile(adminEmail);

        siteReview.setStatus(targetStatus);
        siteReview.setReviewedAt(LocalDateTime.now());
        siteReview.setReviewedByAdmin(admin);

        return toSiteReviewResponse(siteReviewRepository.save(siteReview));
    }

    private Patient getPatient(String patientEmail) {
        return patientRepository.findByUserEmail(patientEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient profile not found"));
    }

    private Admin resolveAdminProfile(String adminEmail) {
        Admin existingAdmin = adminRepository.findByUserEmail(adminEmail).orElse(null);
        if (existingAdmin != null) {
            return existingAdmin;
        }

        User user = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin user not found"));

        if (user.getRole() != null && user.getRole().name().equals("ADMIN")) {
            Admin admin = new Admin();
            admin.setUser(userRepository.getReferenceById(user.getId()));
            return adminRepository.save(admin);
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin profile not found");
    }

    private SiteReview findLatestReview(Long patientId) {
        return siteReviewRepository.findTopByPatient_IdOrderByCreatedAtDesc(patientId).orElse(null);
    }

    private SiteReviewResponse toSiteReviewResponse(SiteReview siteReview) {
        return new SiteReviewResponse(
                siteReview.getId(),
                siteReview.getPatient() != null ? siteReview.getPatient().getId() : null,
                siteReview.getPatientNameSnapshot(),
                siteReview.getPatientPhotoUrlSnapshot(),
                siteReview.getRating(),
                siteReview.getReviewText(),
                siteReview.getStatus().name(),
                siteReview.getCreatedAt(),
                siteReview.getReviewedAt()
        );
    }

    private AdminPendingSiteReviewResponse toAdminPendingResponse(SiteReview siteReview) {
        return new AdminPendingSiteReviewResponse(
                siteReview.getId(),
                siteReview.getPatient() != null ? siteReview.getPatient().getId() : null,
                siteReview.getPatientNameSnapshot(),
                siteReview.getPatientEmailSnapshot(),
                siteReview.getPatientPhotoUrlSnapshot(),
                siteReview.getRating(),
                siteReview.getReviewText(),
                siteReview.getStatus().name(),
                siteReview.getCreatedAt()
        );
    }

    private PublicSiteReviewResponse toPublicResponse(SiteReview siteReview) {
        return new PublicSiteReviewResponse(
                siteReview.getId(),
                siteReview.getPatientNameSnapshot(),
                siteReview.getPatientPhotoUrlSnapshot(),
                siteReview.getRating(),
                siteReview.getReviewText(),
                siteReview.getReviewedAt()
        );
    }

    private String resolvePatientName(Patient patient) {
        String fullName = trimToNull(patient.getFullName());
        if (fullName != null) {
            return fullName;
        }

        User user = patient.getUser();
        return user != null ? resolvePatientEmail(patient) : "Patient";
    }

    private String resolvePatientEmail(Patient patient) {
        User user = patient.getUser();
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient email is required to create a site review");
        }
        return user.getEmail();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
