package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;
import com.healthcare.personal_health_monitoring.dto.DoctorNoteDTO;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.DoctorNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drnote")
public class DoctorNoteController {

    @Autowired
    private DoctorNoteService doctorNoteService;

    @GetMapping(path="/patientViewNote")


    public ResponseEntity<List<DoctorNoteDTO> > getpatientViewNote(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long patientId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                doctorNoteService.getpatientViewNote(patientId)
        );
    }

    @GetMapping(path="/DoctorViewNote/{patientId}")

    public ResponseEntity<List<DoctorNoteDTO> > getDoctorViewNote(@PathVariable long patientId)
    {

        return ResponseEntity.ok(
                doctorNoteService.getpatientViewNote(patientId)
        );
    }
}
