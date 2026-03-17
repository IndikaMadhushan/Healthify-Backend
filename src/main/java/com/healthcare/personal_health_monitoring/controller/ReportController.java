package com.healthcare.personal_health_monitoring.controller;


import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.Report;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.ReportRepository;
import com.healthcare.personal_health_monitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/doctors/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final PatientRepository patientRepository;

    @GetMapping("/{reportId}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long reportId, Authentication auth){
        ensurePatientOwnsReportIfNeeded(reportId, auth);
        return reportService.downloadReport(reportId);
    }

    private void ensurePatientOwnsReportIfNeeded(Long reportId, Authentication auth) {
        boolean isPatient = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_PATIENT"));

        if (!isPatient) {
            return;
        }

        Patient patient = patientRepository.findByUserEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Patient record not found"));

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        if (report.getPatient() == null || report.getPatient().getId() != patient.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only download your own reports");
        }
    }
}
