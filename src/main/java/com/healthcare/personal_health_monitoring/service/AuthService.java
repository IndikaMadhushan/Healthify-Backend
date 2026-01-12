package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.AuthResponse;
import com.healthcare.personal_health_monitoring.dto.DoctorRegisterRequest;
import com.healthcare.personal_health_monitoring.dto.PatientCreateRequest;
import com.healthcare.personal_health_monitoring.dto.PatientRegisterRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    void registerPatient(PatientRegisterRequest req);
    void registerDoctor(DoctorRegisterRequest req,
                        MultipartFile verificationDoc);
    public AuthResponse login(String email, String password);
}
