package com.healthcare.personal_health_monitoring.controller;


import com.healthcare.personal_health_monitoring.dto.ClinicBookRequestDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicBookViewDTO;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.ClinicBookService;
import com.healthcare.personal_health_monitoring.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cbook")
public class ClinicBookController {

    @Autowired
    private ClinicBookService clinicBookService;

    //create new clicnic boojk(only doctors)
    @PostMapping(path = "create/{patient_id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<StandardResponse> createClinicBook(
            @PathVariable(value = "patient_id") Long patientId,
            @RequestBody ClinicBookRequestDTO clinicBookRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long doctorId = userDetails.getUser().getId();
        String message = clinicBookService.createClinicBook(patientId, clinicBookRequestDTO, doctorId);

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201, "created", message),
                HttpStatus.CREATED

        );

    }

    //when doctor click edit but to edit reason or access
    @GetMapping(path = "/{clinicbook_id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<StandardResponse> getReasonAndReason(@PathVariable(value = "clinicbook_id") int id) {
        ClinicBookRequestDTO message = clinicBookService.getReasonAndReason(id);

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "OK", message),
                HttpStatus.OK

        );

    }

    //EDIT CLINIC BOOK CREATED REASON OR ACCESS BY DOCTOR.IF CREATED DOCTOE GIVE ALLOW ACCESS, ANY DOCTOR CAN EDIT.OTHERWISE CANNOT
    @PutMapping(path = "/{clinicbook_id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<StandardResponse> updateReason(@RequestBody ClinicBookRequestDTO clinicBookRequestDTO, @PathVariable(value = "clinicbook_id") int bookid,@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long doctorId = userDetails.getUser().getId();


        String message = clinicBookService.updateReason(clinicBookRequestDTO, bookid,doctorId );

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "ok", message),
                HttpStatus.CREATED

        );

    }

    @DeleteMapping(path = "/{clinicbook_id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<StandardResponse> deleteClinicBook(@PathVariable(value = "clinicbook_id") int bookid,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long doctorId = userDetails.getUser().getId();
        String message = clinicBookService.deleteClinicBook(bookid,doctorId);

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "OK", message),
                HttpStatus.OK

        );

    }

    //get patient all clinic book

//    @GetMapping(path="/{patient_id}")
//    public ResponseEntity<StandardResponse> getPatientClinicBooks(@PathVariable(value = "patient_id") int id) {
//        ClinicBookRequestDTO message = clinicBookService.getReasonAndReason(id);
//
//        return new ResponseEntity<StandardResponse>(
//                new StandardResponse(200, "OK", message),
//                HttpStatus.OK
//
//        );
//
//    }

    //doctor
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<ClinicBookViewDTO> getClinicBook(@PathVariable long patientId) {
         return clinicBookService.getClinicBookDetails(patientId);
    }

    @GetMapping("/patient-clinic")
//    @PreAuthorize("hasRole('PATIENT')")
    public List<ClinicBookViewDTO> getClinicBook(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long patientId = userDetails.getUser().getId();
        return clinicBookService.getPatientClinicBookDetails(patientId);
    }

    @GetMapping("clinic_data/{clinicBookId}")
    public ResponseEntity<ClinicBookViewDTO> getClinicBookById(
            @PathVariable Integer clinicBookId
    ) {
        return ResponseEntity.ok(
                clinicBookService.getClinicBookById(clinicBookId)
        );
    }





}