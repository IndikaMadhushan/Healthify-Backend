package com.healthcare.personal_health_monitoring.controller;


import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final UserRepository repo;

    @GetMapping("/me")
    public Object getMyInfo(Authentication auth){
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return repo.findByEmail(user.getUsername());
    }
}
