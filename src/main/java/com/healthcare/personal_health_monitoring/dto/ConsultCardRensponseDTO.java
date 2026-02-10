package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Medication;
import com.healthcare.personal_health_monitoring.entity.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ConsultCardRensponseDTO {
    private int consultId;


    private String doctorName;
    private String slmc;

//    private Patient patient;

    private String consultReason;

    private LocalDate pagecreatedDate;
    private LocalTime pagecreatedTime;

    private LocalDate updatedDate;
    private LocalTime updatedTime;

}
