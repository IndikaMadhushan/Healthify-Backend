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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        ConsultPageFullDTO data =
                consultService.getConsultPageFullData(consultId);

        return new ResponseEntity<>(
                new StandardResponse(200, "success", data),
                HttpStatus.OK
        );
    }









}
