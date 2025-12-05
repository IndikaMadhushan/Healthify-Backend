package com.healthcare.personal_health_monitoring.controller;


import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final UserRepository repo;

    @GetMapping("/patients")
    public List<User> searchPatients(){
        return repo.findAll().stream()
                .filter(u -> u.getRole() == UserRole.PATIENT)
                .toList();
    }
}
