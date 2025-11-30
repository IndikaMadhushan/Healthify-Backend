package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.UserRequestDTO;
import com.healthcare.personal_health_monitoring.dto.UserResponseDTO;
import com.healthcare.personal_health_monitoring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO req) {
        UserResponseDTO resp = userService.register(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserRequestDTO req) {
        return ResponseEntity.ok(userService.update(id, req));
    }

    // Add disease & allergy endpoints (IDs expected to exist already)
    @PostMapping("/{id}/diseases/{diseaseId}")
    public ResponseEntity<Void> addDisease(@PathVariable Long id, @PathVariable Long diseaseId) {
        userService.addDiseaseToUser(id, diseaseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/allergies/{allergyId}")
    public ResponseEntity<Void> addAllergy(@PathVariable Long id, @PathVariable Long allergyId) {
        userService.addAllergyToUser(id, allergyId);
        return ResponseEntity.ok().build();
    }
}
