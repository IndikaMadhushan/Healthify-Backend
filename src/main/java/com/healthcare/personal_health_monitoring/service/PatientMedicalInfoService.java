package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.PatientMedicalInfoDto;

public interface PatientMedicalInfoService {
    PatientMedicalInfoDto getMedicalInfo(Long patientId);
    PatientMedicalInfoDto saveMedicalInfo(Long patientId, PatientMedicalInfoDto request);
}
