package com.healthcare.personal_health_monitoring.controller;


import com.healthcare.personal_health_monitoring.dto.ClinicBookViewDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicPrescriptionCardListDTO;
import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;
import com.healthcare.personal_health_monitoring.dto.ConsultPageDTO;
import com.healthcare.personal_health_monitoring.repository.ConsultRepo;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @GetMapping
    public ResponseEntity<List<ConsultCardRensponseDTO> > getPatientPrescriptionCards(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long patientId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                consultService.getClinicPagesByClinicBookId(patientId)
        );
    }


}
