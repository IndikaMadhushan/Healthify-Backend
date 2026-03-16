package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Report;
import com.healthcare.personal_health_monitoring.repository.ReportRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.AuditLogService;
import com.healthcare.personal_health_monitoring.service.FileUploadService;
import com.healthcare.personal_health_monitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
        private final PatientRepository patientRepository;
    private final AuditLogService auditLogService;
        private final FileUploadService fileUploadService;
        private final RestTemplate restTemplate = new RestTemplate();

        @Value("${supabase.bucket.medical-files}")
        private String medicalFilesBucket;

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
        public Report uploadReport(
                        Long patientId,
                        String reportType,
                        LocalDate reportDate,
                        MultipartFile file
        ) {
                if (file == null || file.isEmpty()) {
                        throw new RuntimeException("Report file is required");
                }
                if (reportType == null || reportType.isBlank()) {
                        throw new RuntimeException("Report type is required");
                }

                String path = fileUploadService.uploadPrivateFile(
                                file,
                                "medical-files",
                                "reports/patient-" + patientId + "/" + reportType
                );

                Report report = new Report();
                report.setPatient(patientRepository.findById(patientId)
                                .orElseThrow(() -> new RuntimeException("Patient not found")));
                report.setReportType(reportType);
                report.setReportDate(reportDate != null ? reportDate : LocalDate.now());
                report.setFileUrl(path);

                return reportRepository.save(report);
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
                                "Doctor download report ID " + reportId
        );

                byte[] fileBytes = fileUploadService.downloadPrivateFile(
                                "medical-files",
                                report.getFileUrl()
                );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=report-" + reportId)
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }


}
