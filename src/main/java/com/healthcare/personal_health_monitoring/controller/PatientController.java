package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;
import com.healthcare.personal_health_monitoring.service.PatientService;
import com.healthcare.personal_health_monitoring.service.PatientHealthMetricService;
import com.healthcare.personal_health_monitoring.util.BmiUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final PatientHealthMetricService patientHealthMetricService;

    // Create patient - if we want to create via /auth/register ignore this
    @PostMapping
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientCreateRequest req) {
        PatientResponse resp = patientService.createPatient(req);
        return ResponseEntity.ok(resp);
    }

    // Get logged-in patient's profile (patient) or doctor/admin can call but get by email
    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> me(Authentication auth) {
        String email = auth.getName();
        PatientResponse resp = patientService.getMe(email);
        return ResponseEntity.ok(resp);
    }

    // Get patient by id (doctor/admin)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // Update patient — patient can update their own record, doctors/admins can update any
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody PatientUpdateRequest req,
                                                  Authentication auth) {
        // if requester is PATIENT ensure they only update their own record
        boolean isPatient = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));
        if (isPatient) {
            String email = auth.getName();
            PatientResponse target = patientService.getPatientById(id);
            if (!target.getEmail().equalsIgnoreCase(email)) {
                return ResponseEntity.status(403).build();
            }
        }
        PatientResponse resp = patientService.updatePatient(id, req);
        return ResponseEntity.ok(resp);
    }


    @PostMapping("/{id}/profile-image")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image,
            Authentication auth
    ) {
        // ensure patient uploads only their own image
        PatientResponse patient = patientService.getPatientById(id);

        if (!patient.getEmail().equals(auth.getName())) {
            return ResponseEntity.status(403).build();
        }

        String imageUrl = patientService.uploadProfileImage(id, image);
        return ResponseEntity.ok(imageUrl);
    }


    // List all patients (doctor/admin)
    @GetMapping("/all")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<PatientResponse>> listAll() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // Delete (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{patientId}/bmi")
    public BmiResponse getBmiDetails(@PathVariable Long patientId) {
        PatientHealthMetric bmiMetric = patientHealthMetricService
                .getLatestMetric(patientId, HealthMetricType.BMI)
                .orElse(null);

        if (bmiMetric == null || bmiMetric.getValue() == null) {
            return new BmiResponse(null, "NOT_AVAILABLE", "Please update height and weight.");
        }

        double bmi = bmiMetric.getValue();
        String category = BmiUtil.getBmiCategory(bmi);
        String tip = BmiUtil.getHealthTip(category);

        return new BmiResponse(bmi, category, tip);
    }

}
