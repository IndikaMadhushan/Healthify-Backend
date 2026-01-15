package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Report;
import com.healthcare.personal_health_monitoring.repository.ReportRepository;
import com.healthcare.personal_health_monitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public List<Report> getReportByPatient(Long patientId) {
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
}
