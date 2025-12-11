package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Allergy;
import com.healthcare.personal_health_monitoring.repository.AllergyRepository;
import com.healthcare.personal_health_monitoring.service.AllergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;

    @Override
    public Allergy saveAllergy(Allergy allergy) {
        return allergyRepository.save(allergy);
    }

    @Override
    public Optional<Allergy> getAllergyById(Long id) {
        return allergyRepository.findById(id);
    }

    @Override
    public List<Allergy> getAllAllergies() {
        return allergyRepository.findAll();
    }
}
