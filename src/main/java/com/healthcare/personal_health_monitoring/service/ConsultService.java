package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;

import java.util.List;

public interface ConsultService {
    List<ConsultCardRensponseDTO> getPatientPrescriptionCards(Long patientId);


}
