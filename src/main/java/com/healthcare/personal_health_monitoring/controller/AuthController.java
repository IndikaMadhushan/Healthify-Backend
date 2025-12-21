package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.service.AuthService;
import com.healthcare.personal_health_monitoring.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/patient/register")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody PatientRegisterRequest req) {
        authService.registerPatient(req);
        return ResponseEntity.ok(
                "Patient Registerd Successfully"
        );
    }

    @PostMapping("/doctor/register")
    public ResponseEntity<?> registerDoctor(
            @Valid @RequestBody  DoctorRegisterRequest req) {
        authService.registerDoctor(req);
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
}
