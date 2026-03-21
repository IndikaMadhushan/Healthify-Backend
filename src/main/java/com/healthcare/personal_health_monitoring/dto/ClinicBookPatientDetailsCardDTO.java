package com.healthcare.personal_health_monitoring.dto;

// import com.healthcare.personal_health_monitoring.entity.Doctor;
// import com.healthcare.personal_health_monitoring.entity.Patient;
// import jakarta.persistence.Column;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicBookPatientDetailsCardDTO {

    private String visit_reason;
    private Integer age;
    private String PatinetId;
    private String Gender;

}
