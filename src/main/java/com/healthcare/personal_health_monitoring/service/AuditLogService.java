package com.healthcare.personal_health_monitoring.service;

public interface AuditLogService {
    void log(
            String actorEmail,
            String actorRole,
            String action,
            Long patientId,
            String description
    );
}
