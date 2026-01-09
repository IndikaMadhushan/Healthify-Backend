package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorContoller {
    private final PatientService patientService;

    //docot search patient by patient id
    @GetMapping("/patients/{patientId}")
    //@PreAuthorize("hasRole('DOCTOR)")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable String patientId){
        PatientResponse response = patientService.getPatientByPatientId(patientId);

        return ResponseEntity.ok(response);
    }

}
