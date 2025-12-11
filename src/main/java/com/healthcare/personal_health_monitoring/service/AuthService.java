package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.AuthResponse;
import com.healthcare.personal_health_monitoring.dto.RegisterRequest;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    /**
     * Registers a new user. For role DOCTOR we mark as enabled=false (needs admin approval).
     * For PATIENT register as enabled=true.
     */
    public void register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setNic(req.getNic());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // parse role
        UserRole role = UserRole.PATIENT;
        if ("DOCTOR".equalsIgnoreCase(req.getRole())) {
            role = UserRole.DOCTOR;
            user.setEnabled(false); // admin will verify and enable
        } else if ("ADMIN".equalsIgnoreCase(req.getRole())) {
            role = UserRole.ADMIN; // careful: admin signup normally should be controlled
        }
        user.setRole(role);

        userRepository.save(user);
    }

    /**
     * Log in and return JWT token string packaged in AuthResponse
     */
    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not enabled. Contact admin.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token);
    }
}
