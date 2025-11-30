package com.healthcare.personal_health_monitoring.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllergyDTO {
    private Long id;
    private String name;
}
