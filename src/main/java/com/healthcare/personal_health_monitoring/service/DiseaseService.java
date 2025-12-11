package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Disease;

import java.util.List;
import java.util.Optional;

public interface DiseaseService {
    Disease saveDisease(Disease disease);
    Optional<Disease> getDiseaseById(Long id);
    List<Disease> getAllDiseases();
}
