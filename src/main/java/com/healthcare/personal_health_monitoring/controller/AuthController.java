package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.service.AuthService;
import com.healthcare.personal_health_monitoring.service.EmailService;
import com.healthcare.personal_health_monitoring.service.impl.AuthServiceImpl;
import com.healthcare.personal_health_monitoring.util.OtpGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private  final UserRepository userRepository;
    private final EmailService emailService;


    @PostMapping("/patient/register")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody PatientRegisterRequest req) {
        authService.registerPatient(req);
        return ResponseEntity.ok(
                "Patient Registerd Successfully"
        );
    }

    @PostMapping(
            value = "/doctor/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> registerDoctor(
            @ModelAttribute @Valid DoctorRegisterRequest request,
            @RequestPart("verificationDoc") MultipartFile verificationDoc
    ) {
        System.out.println("License number = " + request.getLicenseNumber());

        authService.registerDoctor(request, verificationDoc);
        return ResponseEntity.ok(
                "Doctor registered successfully. Await admin approval."
        );

    };

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(
                authService.login(req.email(), req.password())
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String email,
            @RequestParam String otp){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getEmailOtp() == null) {
            return ResponseEntity.badRequest().body("OTP expired or alredy used");
        }

        if (user.getEmailOtp().equals(otp)) {
            user.setEmailVerified(true);
            user.setEmailOtp(null);
            user.setOtpGeneratedAt(null);
            userRepository.save(user);

            return ResponseEntity.ok("Email Verified Successfully");
        }

        return ResponseEntity.badRequest().body("Invalid otp");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("Email already verified");
        }

        String otp = OtpGenerator.generateOtp();

        user.setEmailOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());

        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok("OTP resent successfully");
    }

}
