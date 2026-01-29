package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Surgery;
import com.healthcare.personal_health_monitoring.entity.Vaccination;
import com.healthcare.personal_health_monitoring.repository.SurgeryRepository;
import com.healthcare.personal_health_monitoring.repository.VaccinationRepository;
import com.healthcare.personal_health_monitoring.service.DoctorPatientHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DoctorPatientHistoryServiceImpl implements DoctorPatientHistoryService {

    private final SurgeryRepository surgeryRepository;
    private final VaccinationRepository vaccinationRepository;

    @Override
    public List<Surgery> getSurgeries(Long patientId) {
        return surgeryRepository.findByPatientId(patientId);
    }

    @Override
    public List<Vaccination> getVaccinations(Long patientId) {
        return vaccinationRepository.findByPatientId(patientId);
    }
}
