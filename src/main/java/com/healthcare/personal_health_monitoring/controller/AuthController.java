package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public String register(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req){
        return service.login(req.email(), req.password());
    }
}

record LoginRequest(String email, String password) {}
