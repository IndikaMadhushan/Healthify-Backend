package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;

    /**
     * Approve doctor by user ID
     */
    @PutMapping("/approve-doctor/{id}")
    public ResponseEntity<String> approveDoctor(@PathVariable Long id) {
        authService.approveDoctor(id);
        return ResponseEntity.ok("Doctor approved successfully");
    }

    @GetMapping("/pending-doctors")
    public List<User> pendingDoctors() {
        return authService.getPendingDoctors();
    }

}
