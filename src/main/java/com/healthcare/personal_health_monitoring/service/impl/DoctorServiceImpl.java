package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.DoctorProfileResponse;
import com.healthcare.personal_health_monitoring.dto.DoctorResponse;
import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.DoctorRepository;
import com.healthcare.personal_health_monitoring.service.DoctorService;
import com.healthcare.personal_health_monitoring.util.DoctorMapper;
import com.healthcare.personal_health_monitoring.util.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    //private final DoctorResponse doctorResponse;

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        // Calculate age from dateOfBirth
        if (doctor.getDateOfBirth() != null) {
            int age = Period.between(doctor.getDateOfBirth(), LocalDate.now()).getYears();
            doctor.setAge(age);
        }
        return doctorRepository.save(doctor);
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public DoctorResponse getDoctorByDoctorId(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Doctor not found with ID: " + doctorId)
                );

        return DoctorMapper.toResponse(doctor);
    }

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByUserEmail(email);
    }

    @Override
    public Optional<Doctor> getDoctorByNic(String nic) {
        return doctorRepository.findByNic(nic);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public DoctorProfileResponse getMyProfile(){

        //get the logged in doctor email from jwt
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        //fetch doctor
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        //maps to response dto
        return new DoctorProfileResponse(
            doctor.getDoctorId(),
            doctor.getFullName(),
            doctor.getUser().getEmail(),
            doctor.getNic(),
            doctor.getSpecialization(),
            doctor.getHospital(),
            doctor.getLicenseNumber(),
            doctor.getPhone(),
            doctor.getAge(),
            doctor.getPhotoUrl()
        );

    }
}
