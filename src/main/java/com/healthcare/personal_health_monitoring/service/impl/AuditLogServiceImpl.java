package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.AuditLog;
import com.healthcare.personal_health_monitoring.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    @Override
    public void log(
            String actorEmail,
            String actorRole,
            String action,
            Long patientId,
            String description
    ) {
        AuditLog log = new AuditLog();
        log.setActorEmail(actorEmail);
        log.setActorRole(actorRole);
        log.setAction(action);
        log.setPatientId(patientId);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());
    }
}
