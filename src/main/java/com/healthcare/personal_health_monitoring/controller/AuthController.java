package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.AuthResponse;
import com.healthcare.personal_health_monitoring.dto.LoginRequest;
import com.healthcare.personal_health_monitoring.dto.RegisterRequest;
import com.healthcare.personal_health_monitoring.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok("Registered. If you registered as a doctor account will remain inactive until admin approval.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        AuthResponse resp = authService.login(req.email(), req.password());
        return ResponseEntity.ok(resp);
    }
}
