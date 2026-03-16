package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.util.NameUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientSearchResponse {
    private Long id;
    private String patientId;
    private String firstName;
    private String secondName;
    private String lastName;
    private String nic;
    private String gender;

    public String getFullName() {
        return NameUtil.combine(firstName, secondName, lastName);
    }
}
