package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicPrescriptionCardDTO {
    private int clinicPageId;

    private String doctorName;   // UpdatedDoctor
    private LocalDate updatedAt; // updatedDate
    private String reason;       // subReason
    private LocalDate createdAt; // pagecreatedDate
}
