package com.healthcare.personal_health_monitoring.service;

import java.time.LocalDate;

public interface PdfService {

    byte[] generatePatientHistoryPdf(
            Long patientId,
            LocalDate from,
            LocalDate to
    );
}
