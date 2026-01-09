package com.healthcare.personal_health_monitoring.util;

import com.healthcare.personal_health_monitoring.dto.DoctorResponse;
import com.healthcare.personal_health_monitoring.entity.Doctor;

import java.time.LocalDate;
import java.time.Period;

public class DoctorMapper {

    private DoctorMapper() {
        // utility class
    }

    /* ---------------- ENTITY → DTO ---------------- */

    public static DoctorResponse toResponse(Doctor d) {
        if (d == null) return null;

        return DoctorResponse.builder()
                .id(d.getId())
                .doctorId(d.getDoctorId())
                .fullName(d.getFullName())
                .email(d.getUser() != null ? d.getUser().getEmail() : null)
                .nic(d.getNic())
                .phone(d.getPhone())
                .specialization(d.getSpecialization())
                .licenseNumber(d.getLicenseNumber())
                .dateOfBirth(d.getDateOfBirth())
                .age(calculateAge(d.getDateOfBirth()))
                .experience(d.getExperience())
                .photoUrl(d.getPhotoUrl())
                .joinedDate(d.getJoinedDate())
                .build();
    }

    /* ---------------- HELPER ---------------- */

    private static Integer calculateAge(LocalDate dob) {
        if (dob == null) return null;
        return Period.between(dob, LocalDate.now()).getYears();
    }
}
