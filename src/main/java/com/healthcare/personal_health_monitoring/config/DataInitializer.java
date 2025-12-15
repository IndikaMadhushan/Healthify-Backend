package com.healthcare.personal_health_monitoring.config;

import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ---------------- ADMIN CREATION ----------------
        if (userRepository.findByEmail("admin@healthcare.com").isEmpty()) {

            User admin = new User();
            admin.setFullName("System Admin");
            admin.setEmail("admin@healthcare.com");
            admin.setNic("ADMIN001");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            admin.setEnabled(true); // Admin is always enabled

            userRepository.save(admin);
        }

        // ---------------- DOCTOR CREATION ----------------
        if (userRepository.findByEmail("doctor@healthcare.com").isEmpty()) {

            User doctor = new User();
            doctor.setFullName("Dr. John Silva");
            doctor.setEmail("doctor@healthcare.com");
            doctor.setNic("DOC001");
            doctor.setPassword(passwordEncoder.encode("doctor123"));
            doctor.setRole(UserRole.DOCTOR);
            doctor.setEnabled(true); // Enabled manually for testing

            userRepository.save(doctor);
        }
    }
}
