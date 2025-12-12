package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FamilyMemberDTO {
    private String name;

    @Past(message = "dob must be a date in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private Boolean alive;
    private String causeOfDeath;
    private String diseases; // comma separated or JSON string
}
