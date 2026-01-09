package com.healthcare.personal_health_monitoring.controller;


import com.healthcare.personal_health_monitoring.dto.ClinicBookRequestDTO;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.ClinicBookService;
import com.healthcare.personal_health_monitoring.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cbook")
public class ClinicBookController {

    @Autowired
    private ClinicBookService clinicBookService;

    @PostMapping(path = "/{patient_id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<StandardResponse> saveClinicBook(
            @PathVariable(value = "patient_id") Long patientId,
            @RequestBody ClinicBookRequestDTO clinicBookRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long doctorId = userDetails.getUser().getId();
        String message = clinicBookService.saveClinicBook(patientId, clinicBookRequestDTO, doctorId);

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201, "created", message),
                HttpStatus.CREATED

        );

    }

    @GetMapping(path = "/{clinicbook_id}")
    public ResponseEntity<StandardResponse> getReason(@PathVariable(value = "clinicbook_id") int id) {
        ClinicBookRequestDTO message = clinicBookService.getReason(id);

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "OK", message),
                HttpStatus.OK

        );

    }

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
}