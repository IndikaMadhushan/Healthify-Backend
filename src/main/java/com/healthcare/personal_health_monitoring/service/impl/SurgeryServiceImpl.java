package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Surgery;
import com.healthcare.personal_health_monitoring.repository.SurgeryRepository;
import com.healthcare.personal_health_monitoring.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurgeryServiceImpl implements SurgeryService {

    private final SurgeryRepository surgeryRepository;

    @Override
    public Surgery saveSurgery(Surgery surgery) {
        return surgeryRepository.save(surgery);
    }

    @Override
    public Optional<Surgery> getSurgeryById(Long id) {
        return surgeryRepository.findById(id);
    }

    @Override
    public List<Surgery> getSurgeriesByPatientId(Long patientId) {
        return surgeryRepository.findByPatientId(patientId);
    }
}
