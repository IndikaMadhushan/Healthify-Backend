package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Report;
import com.healthcare.personal_health_monitoring.repository.AuditLogRepository;
import com.healthcare.personal_health_monitoring.repository.ReportRepository;
import com.healthcare.personal_health_monitoring.service.AuditLogService;
import com.healthcare.personal_health_monitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AuditLogService auditLogService;

    @Override
    public List<Report> getReportByPatient(Long patientId) {

        String doctorEmail =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        auditLogService.log(
                doctorEmail,
                "DOCTOR",
                "VIEW_PATIENT_REPORTS",
                patientId,
                "Doctor viewed patient reports"
        );


        return reportRepository.findByPatientId(patientId);
    }

    @Override
    public List<Report> getReportsByPatientAndType(
            Long patientId,
            String reportType
    ) {
        return reportRepository.findByPatientIdAndReportType(patientId, reportType);
    }

    @Override
    public List<Report> getReportByPatientAndDateRange(
            Long patientId,
            LocalDate from,
            LocalDate to
    ) {
        return reportRepository.findByPatientIdAndReportDateBetween(patientId, from, to);
    }

    @Override
    public ResponseEntity<byte[]> downloadReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        String doctorEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        //audit log
        auditLogService.log(
                doctorEmail,
                "DOCTOR",
                "DOWNLOAD_REPORT",
                report.getPatient().getId(),
                "Doctor download report ID" + reportId
        );

        //load file from storage
        Path filePath = Paths.get(report.getFileUrl().replace("/uploads/", "uploads/"));

        byte[] fileBytes;

        try{
            fileBytes = Files.readAllBytes(filePath);

        }catch (IOException e){
            throw new RuntimeException("Unable to read report file");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=report-" + reportId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }


}
