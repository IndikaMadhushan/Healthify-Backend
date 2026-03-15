package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.service.AuthService;
import com.healthcare.personal_health_monitoring.service.EmailService;
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
                "Registration started. Please verify the OTP sent to your email."
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
                "Registration started. Please verify the OTP sent to your email."
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
        return ResponseEntity.ok(authService.verifyEmailOtp(email, otp));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        return ResponseEntity.ok(authService.resendEmailOtp(email));
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = OtpGenerator.generateOtp();

        user.setEmailOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok("Password reset OTP sent to email" + email);
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<String> confirmPasswordReset(
            @RequestBody @Valid PasswordResetRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmailOtp() == null || user.getOtpGeneratedAt() == null) {
            return ResponseEntity.badRequest()
                    .body("OTP expired or already used");
        }

        // OTP expiry check
        LocalDateTime expiryTime =
                user.getOtpGeneratedAt().plusMinutes(5);

        if (LocalDateTime.now().isAfter(expiryTime)) {
            user.setEmailOtp(null);
            userRepository.save(user);

            return ResponseEntity.badRequest()
                    .body("OTP expired. Please request a new one.");
        }

        // OTP match check
        if (!user.getEmailOtp().equals(request.otp())) {
            return ResponseEntity.badRequest()
                    .body("Invalid OTP");
        }

        // Update password
        user.setPassword(
                authService.encodePassword(request.newPassword())
        );

        // Clear OTP
        user.setEmailOtp(null);

        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }


    @PostMapping("/password-reset/resend")
    public ResponseEntity<String> resendResetOtp(@RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = OtpGenerator.generateOtp();

        user.setEmailOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok("OTP resent successfully");
    }


}
