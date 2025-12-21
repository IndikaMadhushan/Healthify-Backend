package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.AuthResponse;
import com.healthcare.personal_health_monitoring.dto.DoctorRegisterRequest;
import com.healthcare.personal_health_monitoring.dto.PatientRegisterRequest;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import com.healthcare.personal_health_monitoring.repository.DoctorRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.security.JWTUtil;
import com.healthcare.personal_health_monitoring.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//Note: User here is the generic user entity
// (not Patient/Doctor typed). If you use subclassed
// entities and want to persist Patient/Doctor records
// separately, modify register method to create Patient
// or Doctor object and save via appropriate repository.
// This version keeps it simple and safe.


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;


    /**
     * Registers a new user.
     *
     * - PATIENT → enabled immediately
     * - DOCTOR  → disabled until admin approval
     * - ADMIN   → already in the system
     */
    @Override
    @Transactional
    public void registerPatient(PatientRegisterRequest req)  {
        // to Prevent duplicate emails
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use. Use another Email.");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.PATIENT);
        user.setEnabled(true);

        userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setFullName(req.getFullName());
        patient.setDateOfBirth(req.getDateOfBirth());
        patient.setPhone(req.getPhone());

        patientRepository.save(patient);

    }

    @Override
    @Transactional
    public void registerDoctor(DoctorRegisterRequest req){
        // to Prevent duplicate emails
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use. Use another Email.");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.DOCTOR);
        user.setEnabled(false); // Admin need to approve

        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setFullName(req.getFullName());
        doctor.setLicenseNumber(req.getLicenseNumber());
        doctor.setDateOfBirth(req.getDateOfBirth());
        doctor.setPhone(req.getPhone());
        doctor.setSpecialization(req.getSpecialization());

        doctorRepository.save(doctor);
    }


    /**
     * Login user and generate JWT token.
     */
    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // 🔐 Password verification
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 🚫 Block disabled accounts
        if (!user.isEnabled()) {
            throw new RuntimeException("Account not enabled. Contact admin.");
        }

        // 🎟 Generate JWT
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token);
    }


    /**
     * Admin approves a doctor account
     */
    public void approveDoctor(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.DOCTOR) {
            throw new RuntimeException("Only doctor accounts can be approved");
        }

        user.setEnabled(true); // Approve doctor
        userRepository.save(user);
    }

    public List<User> getPendingDoctors() {
        return userRepository.findByRoleAndEnabled(UserRole.DOCTOR, false);
    }



}
