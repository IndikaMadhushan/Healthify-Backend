package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByPatientId(Long patientId);
    List<AuditLog> findByActorEmail(String actorEmail);
}
