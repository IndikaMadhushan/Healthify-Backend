package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.*;
import com.healthcare.personal_health_monitoring.service.FileUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabReportServiceImpl {

    private final LabReportFolderRepository folderRepo;
    private final LabReportFileRepository fileRepo;
    private final PatientRepository patientRepo;
    private final FileUploadService fileUploadService;

    // ── get contents of root or a folder ────────────────────────────────────
    public LabContentsResponse getContents(Long patientId, Long folderId) {
        List<LabReportFolder> folders = folderId == null
                ? folderRepo.findByPatientIdAndParentFolderIsNull(patientId)
                : folderRepo.findByPatientIdAndParentFolderId(patientId, folderId);

        List<LabReportFile> files = folderId == null
                ? fileRepo.findByPatientIdAndFolderIsNull(patientId)
                : fileRepo.findByPatientIdAndFolderId(patientId, folderId);

        LabContentsResponse res = new LabContentsResponse();
        res.setFolders(folders.stream().map(this::mapFolder).collect(Collectors.toList()));
        res.setFiles(files.stream().map(this::mapFile).collect(Collectors.toList()));
        res.setTotalFiles(fileRepo.countByPatientId(patientId));
        res.setTotalFolders(folderRepo.countByPatientId(patientId));
        return res;
    }

    // ── create folder ────────────────────────────────────────────────────────
    @Transactional
    public LabFolderResponse createFolder(Long patientId, String name, Long parentFolderId) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        LabReportFolder folder = new LabReportFolder();
        folder.setPatient(patient);
        folder.setName(name.trim());

        if (parentFolderId != null) {
            LabReportFolder parent = folderRepo.findById(parentFolderId)
                    .orElseThrow(() -> new RuntimeException("Parent folder not found"));
            folder.setParentFolder(parent);
        }

        folderRepo.save(folder);
        return mapFolder(folder);
    }

    // ── upload file ──────────────────────────────────────────────────────────
    @Transactional
    public LabFileResponse uploadFile(Long patientId, Long folderId,
                                      String title, MultipartFile file) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        String url = fileUploadService.uploadFile(file);

        LabReportFile entity = new LabReportFile();
        entity.setPatient(patient);
        entity.setTitle(title != null && !title.isBlank() ? title.trim() : "Untitled");
        entity.setOriginalName(file.getOriginalFilename());
        entity.setFileType(file.getContentType());
        entity.setFileUrl(url);

        if (folderId != null) {
            LabReportFolder folder = folderRepo.findById(folderId)
                    .orElseThrow(() -> new RuntimeException("Folder not found"));
            entity.setFolder(folder);
        }

        fileRepo.save(entity);
        return mapFile(entity);
    }

    // ── delete folder (cascade deletes files inside) ─────────────────────────
    @Transactional
    public void deleteFolder(Long patientId, Long folderId) {
        LabReportFolder folder = folderRepo.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (folder.getPatient().getId() != patientId) {
            throw new RuntimeException("Unauthorized");
        }

        // delete all files inside the folder first
        List<LabReportFile> files = fileRepo.findByPatientIdAndFolderId(patientId, folderId);
        fileRepo.deleteAll(files);
        folderRepo.delete(folder);
    }

    // ── delete file ──────────────────────────────────────────────────────────
    @Transactional
    public void deleteFile(Long patientId, Long fileId) {
        LabReportFile file = fileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (file.getPatient().getId() != patientId) {
            throw new RuntimeException("Unauthorized");
        }

        fileRepo.delete(file);
    }

    // ── mappers ──────────────────────────────────────────────────────────────
    private LabFolderResponse mapFolder(LabReportFolder f) {
        LabFolderResponse r = new LabFolderResponse();
        r.setId(f.getId());
        r.setName(f.getName());
        r.setParentFolderId(f.getParentFolder() != null ? f.getParentFolder().getId() : null);
        r.setCreatedAt(f.getCreatedAt());
        r.setFileCount(fileRepo.findByPatientIdAndFolderId(f.getPatient().getId(), f.getId()).size());
        return r;
    }

    private LabFileResponse mapFile(LabReportFile f) {
        LabFileResponse r = new LabFileResponse();
        r.setId(f.getId());
        r.setTitle(f.getTitle());
        r.setOriginalName(f.getOriginalName());
        r.setFileType(f.getFileType());
        r.setFileUrl(f.getFileUrl());
        r.setFolderId(f.getFolder() != null ? f.getFolder().getId() : null);
        r.setUploadedAt(f.getUploadedAt());
        return r;
    }
}