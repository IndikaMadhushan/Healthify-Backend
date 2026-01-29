package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Report;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByPatientId(Long patientId);

    List<Report> findByPatientIdAndReportType(
            Long patientId,
            String reportType
    );

    List<Report> findByPatientIdAndReportDateBetween(
            Long patientId,
            LocalDate from,
            LocalDate to
    );
}
