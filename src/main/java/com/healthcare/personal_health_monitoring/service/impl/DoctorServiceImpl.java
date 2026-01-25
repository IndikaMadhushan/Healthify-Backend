package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.DoctorProfileResponse;
import com.healthcare.personal_health_monitoring.dto.DoctorResponse;
import com.healthcare.personal_health_monitoring.dto.DoctorUpdateRequest;
import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.DoctorRepository;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.service.DoctorService;
import com.healthcare.personal_health_monitoring.service.EmailService;
import com.healthcare.personal_health_monitoring.util.AgeUtil;
import com.healthcare.personal_health_monitoring.util.DoctorMapper;
import com.healthcare.personal_health_monitoring.util.PatientMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
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

    @Override
    public DoctorProfileResponse updateMyProfile(DoctorUpdateRequest request){
        //identify the loggesd in doctor
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        //get the doctor
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor no found"));

        //update allowed fields
        doctor.setFullName(request.getFullName());
        doctor.setPhone(request.getPhone());
        doctor.setHospital(request.getHospital());
        doctor.setSpecialization(request.getSpecialization());
        if(request.getDateOfBirth() != null) {
            doctor.setAge(AgeUtil.calculateAge(request.getDateOfBirth()));
        }

        //save data
        Doctor saved = doctorRepository.save(doctor);

        //return updated profile
        return new DoctorProfileResponse(
                saved.getDoctorId(),
                saved.getFullName(),
                saved.getUser().getEmail(),
                saved.getNic(),
                saved.getSpecialization(),
                saved.getHospital(),
                saved.getLicenseNumber(),
                saved.getPhone(),
                saved.getAge(),
                saved.getPhotoUrl()
        );
    }

    @Override
    @Transactional
    public void toggleDoctorStatus(String doctorId) {
        // Find doctor by doctorId
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        // Get associated user
        User user = doctor.getUser();
        if (user == null) {
            throw new RuntimeException("User not found for doctor: " + doctorId);
        }

        // Toggle enabled status
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);

        //  Send notification email
         if (user.isEnabled()) {
             emailService.sendAccountActivatedEmail(user.getEmail(), doctor.getFullName());
         } else {
             emailService.sendAccountDeactivatedEmail(user.getEmail(), doctor.getFullName());
         }
    }

    @Override
    public List<DoctorResponse> getPendingDoctors() {
        List<Doctor> pendingDoctors = doctorRepository.findByUserEnabled(false);

        return pendingDoctors.stream()
                .map(DoctorMapper::toResponse)
                .collect(Collectors.toList());
    }
}
