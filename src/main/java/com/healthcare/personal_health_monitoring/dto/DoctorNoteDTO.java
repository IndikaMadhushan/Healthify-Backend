package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Medication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorNoteDTO {
    private String doctorName;
    private String specialization;
    private LocalDate pagecreatedDate;
    private LocalTime pagecreatedTime;
    private String doctorNote;
}

