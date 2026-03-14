package com.healthcare.personal_health_monitoring.controller;


import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.repository.ConsultRepo;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.ConsultService;
import com.healthcare.personal_health_monitoring.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consult")
public class ConsultController {

    @Autowired
    private ConsultService consultService;

    //get all prescription card data by patient that logged
    @GetMapping(path="/get-by-patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<ConsultCardRensponseDTO> > getPatientPrescriptionCards(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long patientId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                consultService.getPatientPrescriptionCards(patientId)
        );
    }

    @GetMapping(path="/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<ConsultCardRensponseDTO> > getPatientPrescriptionCardsByDoctor(@PathVariable long patientId)
    {
        return ResponseEntity.ok(
                consultService.getPatientPrescriptionCards(patientId)
        );
    }


    @GetMapping("/page/{consultId}")
    public ResponseEntity<StandardResponse> getConsultPageFullData(
            @PathVariable int consultId
    ) {
        ConsultPageResponseDTO data =
                consultService.getConsultPageFullData(consultId);

        return new ResponseEntity<>(
                new StandardResponse(200, "success", data),
                HttpStatus.OK
        );
    }

//    page created (done)
    @PostMapping("/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> createConsultPage(
            @PathVariable Long patientId,
            @RequestBody ConsultPageFullDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long doctorId = userDetails.getUser().getId();

        String message = consultService.createConsultPage(patientId, dto, doctorId);

        return ResponseEntity.ok(message);
    }

    // ================= UPDATE CONSULT PAGE =================
    @PutMapping("/{consultId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> updateConsultPage(
            @PathVariable int consultId,
            @RequestBody ConsultPageFullDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long doctorId = userDetails.getUser().getId();

        return ResponseEntity.ok(
                consultService.updateConsultPage(
                        consultId,
                        dto,
                        doctorId
                )
        );
    }


    // ================= DELETE CONSULT PAGE =================
    @DeleteMapping("/{consultId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> deleteConsultPage(
            @PathVariable int consultId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long doctorId = userDetails.getUser().getId();

        return ResponseEntity.ok(
                consultService.deleteConsultPage(
                        consultId,
                        doctorId
                )
        );
    }

    @PostMapping("/request-edit/{consultId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> requestEditApproval(
            @PathVariable int consultId){

        consultService.requestEditApproval(consultId);

        return ResponseEntity.ok("APPROVAL_EMAIL_SENT");
    }


    @GetMapping("/approve-edit/{consultId}")
    public ResponseEntity<String> approveEdit(
            @PathVariable int consultId){

        return ResponseEntity.ok(
                consultService.approveEditByPatient(consultId)
        );
    }

//    @PostMapping("/{patientId}")
//    @PreAuthorize("hasRole('DOCTOR')")
//    public ResponseEntity<String> createConsultPage(
//            @PathVariable Long patientId,
//            @RequestBody ConsultPageFullDTO dto,
//            @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//
//        Long doctorId = userDetails.getUser().getId();
//
//        String message = consultService.createConsultPage(patientId, dto, doctorId);
//
//        return ResponseEntity.ok(message);
//    }






}
