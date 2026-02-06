package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.ClinicPageDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicPrescriptionCardDTO;
import com.healthcare.personal_health_monitoring.entity.ClinicPage;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.ClinicPageService;
import com.healthcare.personal_health_monitoring.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cpage")
public class ClinicPageController {

    @Autowired
    private ClinicPageService clinicPageService;

    //create a new clinic page
    @PostMapping(path = "/{clinic_book_id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<StandardResponse> saveClinicPage(@PathVariable(value = "clinic_book_id") int clinicBookId, @RequestBody ClinicPageDTO clinicPageDTO,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long doctor_id = userDetails.getUser().getId();
        String message = clinicPageService.saveClinicPage(clinicBookId, clinicPageDTO, doctor_id);

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201, "created", message),
                HttpStatus.CREATED

        );

    }


    @PutMapping("/{clinicPageId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> updateClinicPage(@PathVariable (value = "clinicPageId") int clinicPageId, @RequestBody ClinicPageDTO clinicPageDTO,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long doctor_id = userDetails.getUser().getId();
        return ResponseEntity.ok(
                clinicPageService.updateClinicPage(clinicPageId, clinicPageDTO, doctor_id)
        );
    }


    @DeleteMapping(path = "/{clinicPageId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<StandardResponse> deleteClinicPage(@PathVariable int clinicPageId,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long doctorId = userDetails.getUser().getId();
        String message = clinicPageService.deleteClinicPage(clinicPageId, doctorId);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(204, "deleted", message),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping(path = "/{clinicPageId}")
    public ResponseEntity<StandardResponse> getClinicPageData(@PathVariable int clinicPageId) {
        ClinicPageDTO message = clinicPageService.getClinicPageData(clinicPageId);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201, "created", message),
                HttpStatus.CREATED

        );
    }

    @PostMapping("/request-edit/{clinicPageId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> requestEditApproval(@PathVariable int clinicPageId,@AuthenticationPrincipal CustomUserDetails userDetails)

    {
        Long doctor_id = userDetails.getUser().getId();
        clinicPageService.requestEditApproval(clinicPageId,doctor_id);
        return ResponseEntity.ok("APPROVAL_EMAIL_SENT");
    }

    @GetMapping ("/approve-edit/{clinicPageId}")
    public ResponseEntity<String> approveEdit(@PathVariable int clinicPageId) {
        return ResponseEntity.ok(
                clinicPageService.approveEditByPatient(clinicPageId)
        );
    }

    @GetMapping("/clinic-books/{clinicBookId}/pages")
    public List<ClinicPrescriptionCardDTO> getClinicPages(
            @PathVariable int clinicBookId) {

        return clinicPageService.getPagesByClinicBook(clinicBookId);
    }

}