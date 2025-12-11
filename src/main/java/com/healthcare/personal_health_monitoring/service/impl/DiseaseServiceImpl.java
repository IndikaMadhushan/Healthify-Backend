package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Disease;
import com.healthcare.personal_health_monitoring.repository.DiseaseRepository;
import com.healthcare.personal_health_monitoring.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    @Override
    public Disease saveDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public Optional<Disease> getDiseaseById(Long id) {
        return diseaseRepository.findById(id);
    }

    @Override
    public List<Disease> getAllDiseases() {
        return diseaseRepository.findAll();
    }
}
