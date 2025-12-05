package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.DiseaseDTO;
import com.healthcare.personal_health_monitoring.entity.Disease;
import com.healthcare.personal_health_monitoring.repository.DiseaseRepository;
import com.healthcare.personal_health_monitoring.service.DiseaseService;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public DiseaseDTO create(String name) {
        diseaseRepository.findByNameIgnoreCase(name).ifPresent(d -> {
            throw new IllegalArgumentException("Disease already exists: " + name);
        });
        Disease d = Disease.builder().name(name).build();
        Disease saved = diseaseRepository.save(d);
        return DiseaseDTO.builder().id(saved.getId()).name(saved.getName()).build();
    }

    @Override
    public List<DiseaseDTO> listAll() {
        return diseaseRepository.findAll().stream()
                .map(d -> DiseaseDTO.builder().id(d.getId()).name(d.getName()).build())
                .collect(Collectors.toList());
    }
}
