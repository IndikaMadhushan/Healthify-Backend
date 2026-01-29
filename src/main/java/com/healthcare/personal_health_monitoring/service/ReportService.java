package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Report;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<Report> getReportByPatient(Long patientId);

    List<Report> getReportsByPatientAndType(Long patientId, String reportType);

    List<Report> getReportByPatientAndDateRange(
            Long patientId,
            LocalDate from,
            LocalDate to
    );

    ResponseEntity<byte[]> downloadReport(Long reportId);
}
