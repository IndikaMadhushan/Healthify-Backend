package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient savePatient(Patient patient);
    Optional<Patient> getPatientById(Long id);
    Optional<Patient> getPatientByEmail(String email);
    Optional<Patient> getPatientByNic(String nic);
    List<Patient> getAllPatients();
    void deletePatient(Long id);
}
