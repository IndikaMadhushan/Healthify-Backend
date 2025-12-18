package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.AuthResponse;
import com.healthcare.personal_health_monitoring.dto.RegisterRequest;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

//Note: User here is the generic user entity
// (not Patient/Doctor typed). If you use subclassed
// entities and want to persist Patient/Doctor records
// separately, modify register method to create Patient
// or Doctor object and save via appropriate repository.
// This version keeps it simple and safe.


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    /**
     * Registers a new user.
     *
     * - PATIENT → enabled immediately
     * - DOCTOR  → disabled until admin approval
     * - ADMIN   → allowed but should normally be restricted
     */
    public void register(RegisterRequest req) {

        // Prevent duplicate emails
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();

        // ✅ REQUIRED FIELD (fixes your 500 error)
        user.setFullName(req.getFullName());

        user.setEmail(req.getEmail());
        user.setNic(req.getNic());

        // 🔐 Always store encoded passwords
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // 🎯 Default role = PATIENT
        UserRole role = UserRole.PATIENT;
        user.setEnabled(true); // patients enabled by default

        //  Role handling
        if ("DOCTOR".equalsIgnoreCase(req.getRole())) {
            role = UserRole.DOCTOR;
            user.setEnabled(false); // admin approval needed
        }
        else if ("ADMIN".equalsIgnoreCase(req.getRole())) {
            role = UserRole.ADMIN;
            // ⚠ In real systems, admin registration should be restricted
        }

        user.setRole(role);

        // 💾 Persist user
        userRepository.save(user);

        if (role == UserRole.PATIENT) {

            Patient patient = new Patient();

            // fields from User
            patient.setFullName(req.getFullName());
            patient.setEmail(req.getEmail());
            patient.setNic(req.getNic());
            patient.setPassword(passwordEncoder.encode(req.getPassword()));
            patient.setRole(UserRole.PATIENT);
            patient.setEnabled(true);

//            // optional patient-specific fields
//            patient.setDateOfBirth(req.getDateOfBirth());

            patientRepository.save(patient);
        }


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
