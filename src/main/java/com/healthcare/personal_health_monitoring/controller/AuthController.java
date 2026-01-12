package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.service.AuthService;
import com.healthcare.personal_health_monitoring.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
