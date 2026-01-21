package com.healthcare.personal_health_monitoring.dto;

import com.healthcare.personal_health_monitoring.entity.AccessControlClinic;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClinicBookRequestDTO {

    private String visit_reason;
    private AccessControlClinic accessControl;

}