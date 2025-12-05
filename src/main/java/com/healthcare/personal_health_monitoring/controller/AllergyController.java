package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.AllergyDTO;
import com.healthcare.personal_health_monitoring.service.AllergyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/allergies")
public class AllergyController {

    private final AllergyService allergyService;

    public AllergyController(AllergyService allergyService) { this.allergyService = allergyService; }

    @PostMapping
    public ResponseEntity<AllergyDTO> create(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required");
        AllergyDTO dto = allergyService.create(name.trim());
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<AllergyDTO>> list() {
        return ResponseEntity.ok(allergyService.listAll());
    }
}
