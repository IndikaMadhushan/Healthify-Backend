package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Surgery;

import java.util.List;
import java.util.Optional;

public interface SurgeryService {
    Surgery saveSurgery(Surgery surgery);
    Optional<Surgery> getSurgeryById(Long id);
    List<Surgery> getSurgeriesByPatientId(Long patientId);
}
