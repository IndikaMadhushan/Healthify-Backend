package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
@Embeddable
public class FamilyMember {

    private String name;
    private LocalDate dob;
    private Boolean alive;
    private String causeOfDeath; // if not alive
    private String diseases; // comma-separated list of diseases or JSON

    // Age calculated dynamically
    @Transient
    public Integer getAge() {
        if (dob == null) return null;
        return Period.between(dob, LocalDate.now()).getYears();
    }
}
