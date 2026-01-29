package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.AuditLog;
import com.healthcare.personal_health_monitoring.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AdminAuditController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    @GetMapping("/patient/{patientId}")
    public List<AuditLog> getLogsByPatient(@PathVariable Long patientId) {
        return auditLogRepository.findByPatientId(patientId);
    }

    @GetMapping("/doctor")
    public List<AuditLog> getLogsByDoctor(
            @RequestParam String email
    ) {
        return auditLogRepository.findByActorEmail(email);
    }
}
