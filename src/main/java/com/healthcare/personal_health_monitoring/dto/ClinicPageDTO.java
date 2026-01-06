package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClinicPageDTO {



    private String subReason;

    private String clinicExaming;

    private String clinicSuggestTest;

    private String clinicDoctorNote;

    private Date nextClinic;
    private List<MedicationDTO> medication = new ArrayList<>();
    private HealthMetricRequestSetDTO healthMetricRequestSetDTO;
}
