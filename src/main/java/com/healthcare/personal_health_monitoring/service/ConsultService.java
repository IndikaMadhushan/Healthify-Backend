package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;
import com.healthcare.personal_health_monitoring.dto.ConsultPageFullDTO;
import com.healthcare.personal_health_monitoring.dto.ConsultPageResponseDTO;

import java.util.List;

public interface ConsultService {
    List<ConsultCardRensponseDTO> getPatientPrescriptionCards(Long patientId);

    ConsultPageResponseDTO getConsultPageFullData(int consultId);

    String updateConsultPage(int consultId, ConsultPageFullDTO dto, Long doctorId);

    String deleteConsultPage(int consultId, Long doctorId);

    void requestEditApproval(int consultId);

    String approveEditByPatient(int consultId);

    String createConsultPage(Long patientId, ConsultPageFullDTO dto, Long doctorId);
}
