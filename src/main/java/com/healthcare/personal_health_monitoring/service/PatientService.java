package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.PatientCreateRequest;
import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.dto.PatientUpdateRequest;
import com.healthcare.personal_health_monitoring.entity.Patient;

import java.util.List;

public interface PatientService {
    PatientResponse createPatient(PatientCreateRequest request);
    PatientResponse updatePatient(Long id, PatientUpdateRequest request);
    PatientResponse getPatientById(Long id);
    PatientResponse getPatientByPatientId(String patiendId);
    PatientResponse getPatientByEmail(String email);
    PatientResponse getMe(String email);
    List<PatientResponse> getAllPatients();
    void deletePatient(Long id);
    List<Patient> searchPatients(String query);
}
