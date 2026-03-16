package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.impl.LabReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lab-reports")
@RequiredArgsConstructor
public class LabReportController {

    private final LabReportServiceImpl labService;
    private final PatientRepository patientRepo;

    // ── PATIENT: get own root or folder contents
    // GET /api/lab-reports/my?folderId=5   (folderId optional)
    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<LabContentsResponse> getMyContents(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long folderId) {

        Long patientId = resolvePatientId(userDetails.getUser().getId());
        return ResponseEntity.ok(labService.getContents(patientId, folderId));
    }

    // ── DOCTOR: get a searched patient's contents
    // GET /api/lab-reports/patient/{patientId}?folderId=5
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<LabContentsResponse> getPatientContents(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long folderId) {

        return ResponseEntity.ok(labService.getContents(patientId, folderId));
    }

    // ── PATIENT: create folder
    // POST /api/lab-reports/my/folders
    // Body: { "name": "Blood Tests", "parentFolderId": null }
    @PostMapping("/my/folders")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<LabFolderResponse> createFolder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String name,
            @RequestParam(required = false) Long parentFolderId) {

        Long patientId = resolvePatientId(userDetails.getUser().getId());
        return ResponseEntity.ok(labService.createFolder(patientId, name, parentFolderId));
    }

    // ── PATIENT: upload file
    // POST /api/lab-reports/my/files  (multipart)
    // Parts: file, title (text), folderId (optional number)
    @PostMapping("/my/files")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<LabFileResponse> uploadFile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "folderId", required = false) Long folderId) {

        Long patientId = resolvePatientId(userDetails.getUser().getId());
        return ResponseEntity.ok(labService.uploadFile(patientId, folderId, title, file));
    }

    // ── PATIENT: delete folder
    // DELETE /api/lab-reports/my/folders/{folderId}
    @DeleteMapping("/my/folders/{folderId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> deleteFolder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long folderId) {

        Long patientId = resolvePatientId(userDetails.getUser().getId());
        labService.deleteFolder(patientId, folderId);
        return ResponseEntity.ok("Folder deleted");
    }

    // ── PATIENT: delete file
    // DELETE /api/lab-reports/my/files/{fileId}
    @DeleteMapping("/my/files/{fileId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> deleteFile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long fileId) {

        Long patientId = resolvePatientId(userDetails.getUser().getId());
        labService.deleteFile(patientId, fileId);
        return ResponseEntity.ok("File deleted");
    }

    // ── helper: User id → Patient row id
    private Long resolvePatientId(Long userId) {
        Patient p = patientRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient record not found"));
        return p.getId();
    }
}