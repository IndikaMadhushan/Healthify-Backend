package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.DoctorResponse;
import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.service.DoctorService;
import com.healthcare.personal_health_monitoring.service.PatientService;
import com.healthcare.personal_health_monitoring.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthServiceImpl authServiceImpl;
    private final PatientService patientService;
    private final DoctorService doctorService;

    // Approve doctor by user ID

    @PutMapping("/approve-doctor/{id}")
    public ResponseEntity<String> approveDoctor(@PathVariable Long id) {
        authServiceImpl.approveDoctor(id);
        return ResponseEntity.ok("Doctor approved successfully");
    }

    @GetMapping("/pending-doctors")
    public List<DoctorResponse> pendingDoctors() {
        return doctorService.getPendingDoctors();
    }

    @GetMapping("/patients/{patientId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable String patientId) {

        PatientResponse response = patientService.getPatientByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/patients/search")
//    public ResponseEntity<PatientResponse> searchPatient(
//            @RequestParam String searchType,
//            @RequestParam String query
//    ){
//
//    }

    @GetMapping("/doctors/{doctorId}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> getdoctorById(
            @PathVariable String doctorId) {

        DoctorResponse response = doctorService.getDoctorByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/doctors/search")
//    public ResponseEntity<DoctorResponse> searchDoctor(
//            @RequestParam String searchType,
//            @RequestParam String query
//    ){
//
//    }

    // Reject doctor registration
    @DeleteMapping("/reject-doctor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> rejectDoctor(@PathVariable Long id) {
        authServiceImpl.rejectDoctor(id);
        return ResponseEntity.ok("Doctor registration rejected");
    }

    // Toggle doctor account status active or disable
    @PutMapping("/doctors/{doctorId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> toggleDoctorStatus(@PathVariable String doctorId) {
        doctorService.toggleDoctorStatus(doctorId);
        return ResponseEntity.ok("Doctor status updated");
    }

    // Toggle patient account status active or disable
    @PutMapping("/patients/{patientId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> togglePatientStatus(@PathVariable String patientId) {
        patientService.togglePatientStatus(patientId);
        return ResponseEntity.ok("Patient status updated");
    }

}





