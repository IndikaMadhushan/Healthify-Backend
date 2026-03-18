package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.ReportResponseDto;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.Report;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.FileUploadService;
import com.healthcare.personal_health_monitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/reports")
@RequiredArgsConstructor
public class DoctorReportController {
    private final ReportService reportService;
    private final PatientRepository patientRepository;
    private final FileUploadService fileUploadService;

    @Value("${supabase.bucket.medical-files}")
    private String medicalFilesBucket;

    //get all reports of a patient
    @GetMapping("/patient/{patientId}")
    public List<ReportResponseDto> getPatientReport(@PathVariable Long patientId, Authentication auth){
        ensurePatientOwnsRecordIfNeeded(patientId, auth);
        return reportService.getReportByPatient(patientId).stream()
                .map(this::toResponse)
                .toList();
    }

    //filter report by type
    @GetMapping("/patient/{patientId}/type")
    public List<ReportResponseDto> getReportByType(
            @PathVariable Long patientId,
            @RequestParam String reportType,
            Authentication auth
    ) {
        ensurePatientOwnsRecordIfNeeded(patientId, auth);
        return reportService.getReportsByPatientAndType(patientId, reportType).stream()
                .map(this::toResponse)
                .toList();
    }

        @PostMapping(
            value = "/patient/{patientId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        public ReportResponseDto uploadReport(
            @PathVariable Long patientId,
            @RequestParam String reportType,
            @RequestParam(required = false) LocalDate reportDate,
            @RequestParam MultipartFile file,
            Authentication auth
        ) {
        ensurePatientOwnsRecordIfNeeded(patientId, auth);
        return toResponse(reportService.uploadReport(patientId, reportType, reportDate, file));
        }

    @GetMapping("/patient/{patientId}/date-range")
    public List<ReportResponseDto> getReportByDateRange(
            @PathVariable Long patientId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            Authentication auth
            ){
        ensurePatientOwnsRecordIfNeeded(patientId, auth);
        return reportService.getReportByPatientAndDateRange(patientId, from, to).stream()
                .map(this::toResponse)
                .toList();
    }

    private void ensurePatientOwnsRecordIfNeeded(Long patientId, Authentication auth) {
        boolean isPatient = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_PATIENT"));

        if (!isPatient) {
            return;
        }

        Patient patient = patientRepository.findByUserEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Patient record not found"));

        if (patient.getId() != patientId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your own reports");
        }
    }

    private ReportResponseDto toResponse(Report report) {
        return new ReportResponseDto(
                report.getId(),
                report.getPatient() != null ? report.getPatient().getId() : null,
                report.getReportType(),
                report.getReportDate(),
                resolveAccessibleFileUrl(report.getFileUrl())
        );
    }

    private String resolveAccessibleFileUrl(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            return storedPath;
        }
        if (storedPath.startsWith("http://") || storedPath.startsWith("https://")) {
            return storedPath;
        }
        return fileUploadService.createSignedUrl(medicalFilesBucket, storedPath, 3600);
    }
}
