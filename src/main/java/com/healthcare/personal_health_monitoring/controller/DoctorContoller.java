package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.DoctorProfileResponse;
import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.dto.PatientSearchResponse;
import com.healthcare.personal_health_monitoring.service.DoctorService;
import com.healthcare.personal_health_monitoring.service.PatientService;
import com.healthcare.personal_health_monitoring.util.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorContoller {
    private final PatientService patientService;
    private final DoctorService doctorService;

    //docot search patient by patient id
    @GetMapping("/patients/{patientId}")
    //@PreAuthorize("hasRole('DOCTOR)")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable String patientId){
        PatientResponse response = patientService.getPatientByPatientId(patientId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/patients/search")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<PatientSearchResponse> searchPatient(@RequestParam String query){
            return patientService.searchPatients(query)
                    .stream()
                    .map(PatientMapper::toSearchResponse)
                    .toList();
    }

    @GetMapping("/me")
    public ResponseEntity<DoctorProfileResponse> getMyProfile(){
        return ResponseEntity.ok(doctorService.getMyProfile());
    }

}
