package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.AuthResponse;
import com.healthcare.personal_health_monitoring.dto.DoctorRegisterRequest;
import com.healthcare.personal_health_monitoring.dto.PatientCreateRequest;
import com.healthcare.personal_health_monitoring.dto.PatientRegisterRequest;

public interface AuthService {
    void registerPatient(PatientRegisterRequest req);
    void registerDoctor(DoctorRegisterRequest req);
    public AuthResponse login(String email, String password);
}
