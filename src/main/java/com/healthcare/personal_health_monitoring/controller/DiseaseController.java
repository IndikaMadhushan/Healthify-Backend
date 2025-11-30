package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.DiseaseDTO;
import com.healthcare.personal_health_monitoring.service.DiseaseService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    private final DiseaseService diseaseService;

    public DiseaseController(DiseaseService diseaseService) { this.diseaseService = diseaseService; }

    @PostMapping
    public ResponseEntity<DiseaseDTO> create(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required");
        DiseaseDTO dto = diseaseService.create(name.trim());
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<DiseaseDTO>> list() {
        return ResponseEntity.ok(diseaseService.listAll());
    }
}
