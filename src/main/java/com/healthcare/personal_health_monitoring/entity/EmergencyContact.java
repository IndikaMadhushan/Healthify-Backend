package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class EmergencyContact {
    private String name;
    private String phoneNumber;
    private String relationship;
}
