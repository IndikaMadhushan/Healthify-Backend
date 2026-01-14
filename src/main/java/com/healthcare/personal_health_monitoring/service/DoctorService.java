package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.DoctorResponse;
import com.healthcare.personal_health_monitoring.entity.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    Optional<Doctor> getDoctorById(Long id);
    DoctorResponse getDoctorByDoctorId(String doctorId);
    Optional<Doctor> getDoctorByEmail(String email);
    Optional<Doctor> getDoctorByNic(String nic);
    List<Doctor> getAllDoctors();
    void deleteDoctor(Long id);
    //DoctorProfileRe
}
