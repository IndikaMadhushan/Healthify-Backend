package com.healthcare.personal_health_monitoring.config;

import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.DoctorRepository;
import com.healthcare.personal_health_monitoring.repository.IdSequenceRepository;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorRepository doctorRepository;

    @Override
    public void run(String... args) {

        // ---------------- ADMIN CREATION ----------------
        if (userRepository.findByEmail("admin@healthcare.com").isEmpty()) {

            User admin = new User();
            //admin.setFullName("System Admin");
            admin.setEmail("admin@healthcare.com");
           // admin.setNic("ADMIN001");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            admin.setEnabled(true); // Admin is always enabled

            userRepository.save(admin);
        }

        // ---------------- DOCTOR CREATION ----------------
        if (userRepository.findByEmail("doctor@healthcare.com").isEmpty()) {

            Doctor doctor = new Doctor();
            doctor.setFullName("Dr. John Silva");
            doctor.setEmail("doctor@healthcare.com");
            doctor.setNic("DOC001");
            doctor.setPass(passwordEncoder.encode("doctor123"));
            doctor.setRole(UserRole.DOCTOR);
            doctor.setEnabled(true); // Enabled manually for testing

            doctorRepository.save(doctor);
        }
    }

    @Bean
    CommandLineRunner initSequences(IdSequenceRepository repo) {
        return args -> {
            repo.findById(SequenceType.PATIENT)
                    .orElseGet(() -> repo.save(new IdSequence(SequenceType.PATIENT, 1L)));

            repo.findById(SequenceType.DOCTOR)
                    .orElseGet(() -> repo.save(new IdSequence(SequenceType.DOCTOR, 1L)));
        };
    }

}
