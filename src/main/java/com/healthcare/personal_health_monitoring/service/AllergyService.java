package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Allergy;

import java.util.List;
import java.util.Optional;

public interface AllergyService {
    Allergy saveAllergy(Allergy allergy);
    Optional<Allergy> getAllergyById(Long id);
    List<Allergy> getAllAllergies();
}
