package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BmiResponse {

    private Double bmi;
    private String category;
    private String healthTip;
}
