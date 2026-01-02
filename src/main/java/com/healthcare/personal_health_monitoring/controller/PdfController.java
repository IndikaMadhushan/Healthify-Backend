package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/doctor/patients")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;

    @GetMapping("/{patientId}/history/pdf")
    public ResponseEntity<byte[]> downloadHistoryPdf(
            @PathVariable Long patientId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {

        byte[] pdf = pdfService.generatePatientHistoryPdf(patientId, from, to);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=patient-history.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

