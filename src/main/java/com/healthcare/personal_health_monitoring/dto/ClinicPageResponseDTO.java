package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicPageResponseDTO {
    private ClinicPageDTO clinicPageDTO;
    private List<HealthMetricRequestSetDTO> healthMetricRequestSetDTO;
}
