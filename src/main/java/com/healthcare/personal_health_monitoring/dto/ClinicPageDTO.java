package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClinicPageDTO {

    private int clinicPageId;
    private String subReason;

    private String clinicExaming;

    private String clinicSuggestTest;

    private String clinicDoctorNote;

    private Date nextClinic;
    private List<MedicationDTO> medication = new ArrayList<>();
    private HealthMetricRequestSetDTO healthMetricRequestSetDTO;
    private String CreatedDoctor;
    private String SLMC;


    private String PatientName;
    private Integer PatientAge;
    private String PatientGender;

    private LocalDate pagecreatedDate;
    private LocalTime pagecreatedTime;

}
