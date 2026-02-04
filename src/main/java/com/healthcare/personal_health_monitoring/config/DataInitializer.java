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

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorRepository doctorRepository;

    @Override
    public void run(String... args) {

        //ADMIN CREATION
        if (userRepository.findByEmail("admin@healthcare.com").isEmpty()) {

            User admin = new User();
            //admin.setFullName("System Admin");
            admin.setEmail("admin@healthcare.com");
            // admin.setNic("ADMIN001");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            admin.setEmailVerified(true);
            admin.setOtpGeneratedAt(LocalDateTime.now());
            admin.setEnabled(true); // Admin is always enabled

            userRepository.save(admin);
        }
//
//        //ADMIN CREATION
//        if (userRepository.findByEmail("doctor@healthcare.com").isEmpty()) {
//
//            User doctor = new User();
//            doctor.setEmail("doctor@healthcare.com");
//            doctor.setPassword(passwordEncoder.encode("doctor123"));
//            doctor.setRole(UserRole.DOCTOR);
//            doctor.setEnabled(true);
//            doctor.setEmailVerified(true); // manually verified for testing
//            doctor.setOtpGeneratedAt(LocalDateTime.now());
//
//            userRepository.save(doctor);
//        }

        //  DOCTOR CREATION
//        if (userRepository.findByEmail("doctor@healthcare.com").isEmpty()) {
//
//            Doctor doctor = new Doctor();
//            doctor.setFullName("Dr. John Silva");
//            doctor.setEmail("doctor@healthcare.com");
//            doctor.setNic("DOC001");
//            doctor.setPass(passwordEncoder.encode("doctor123"));
//            doctor.setRole(UserRole.DOCTOR);
//            doctor.setEnabled(true); // Enabled manually for testing
//
//            doctorRepository.save(doctor);
//        }

        if (userRepository.findByEmail("doctorhealth@healthcare.com").isEmpty()) {
            User user = new User();
            user.setEmail("doctorhealth@healthcare.com");
            user.setPassword(passwordEncoder.encode("doctor123"));
            user.setRole(UserRole.DOCTOR);
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setOtpGeneratedAt(LocalDateTime.now());

            Doctor doctor = new Doctor();
            doctor.setUser(user); // VERY IMPORTANT (MapsId)
            doctor.setFullName("Dr. John Silva");
            doctor.setDoctorId("DOC001");
            doctor.setGender("MALE");
            doctor.setHospital("General Hospital Colombo");
            doctor.setLicenseNumber("SLMC-123456");
            doctor.setSpecialization("Cardiology");
            doctor.setPhone("0771234567");
            doctor.setCountry("Sri Lanka");

            doctorRepository.save(doctor); // saves BOTH user + doctor
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
