package com.healthcare.personal_health_monitoring.util;

import com.healthcare.personal_health_monitoring.dto.DoctorResponse;
import com.healthcare.personal_health_monitoring.entity.Doctor;

import java.time.LocalDate;
import java.time.Period;

public class DoctorMapper {

    private DoctorMapper() {
        // utility class
    }

    // ENTITY → DTO

    public static DoctorResponse toResponse(Doctor doctor) {
        if (doctor == null) return null;

        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setDoctorId(doctor.getDoctorId());
        response.setFullName(doctor.getFullName());
        response.setNic(doctor.getNic());
        response.setPhone(doctor.getPhone());
        response.setGender(doctor.getGender());
        response.setSpecialization(doctor.getSpecialization());
        response.setHospital(doctor.getHospital());
        response.setLicenseNumber(doctor.getLicenseNumber());
        response.setAge(doctor.getAge());
        response.setPhotoUrl(doctor.getPhotoUrl());
        response.setVerificationDocUrl(doctor.getVerificationDocUrl());

        if (doctor.getUser() != null) {
            response.setEmail(doctor.getUser().getEmail());
            response.setEnabled(doctor.getUser().isEnabled());
            response.setRole(doctor.getUser().getRole().name());
        }

        return response;
    }


}
