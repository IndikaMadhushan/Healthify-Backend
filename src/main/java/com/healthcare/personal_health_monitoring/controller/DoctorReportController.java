package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.Report;
import com.healthcare.personal_health_monitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/reports")
@RequiredArgsConstructor
public class DoctorReportController {
    private final ReportService reportService;

    //get all reports of a patient
    @GetMapping("/patient/{patientId}")
    public List<Report> getPatientReport(@PathVariable Long patientId){
        return reportService.getReportByPatient(patientId);
    }

    //filter report by type
    @GetMapping("/patient/{patientId}/type")
    public List<Report> getReportByType(
            @PathVariable Long patientId,
            @RequestParam String reportType
    ) {
        return reportService.getReportsByPatientAndType(
                patientId, reportType
        ) ;
    }

        @PostMapping(
            value = "/patient/{patientId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        public Report uploadReport(
            @PathVariable Long patientId,
            @RequestParam String reportType,
            @RequestParam(required = false) LocalDate reportDate,
            @RequestParam MultipartFile file
        ) {
        return reportService.uploadReport(patientId, reportType, reportDate, file);
        }

    public List<Report> getReportByDateRange(
            @PathVariable Long patientId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
            ){
        return reportService.getReportByPatientAndDateRange(patientId, from, to);
    }
}
