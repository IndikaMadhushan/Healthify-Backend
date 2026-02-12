package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Data
//@AllArgsConstructor
//public class ClinicPrescriptionCardListDTO {
//    private int id;
//    private int clinicBookId;
//
//    private String createdAt;
//    private String updatedAt;
//
//    private String updatedBy;
//
//    private String reason;
//
//    private String CreatedDoctor;
//
//
//}

@Data
@AllArgsConstructor
public class ClinicPrescriptionCardListDTO {

    private int id;
    private int clinicBookId;

    private String createdAt;
    private String updatedAt;

    private String updatedBy;
    private String reason;

    private String createdDoctor; // ✅ camelCase
}
