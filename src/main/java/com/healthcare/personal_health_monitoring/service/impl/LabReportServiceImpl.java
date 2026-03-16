package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.LabContentsResponse;
import com.healthcare.personal_health_monitoring.dto.LabFileResponse;
import com.healthcare.personal_health_monitoring.dto.LabFolderResponse;
import com.healthcare.personal_health_monitoring.entity.LabReportFile;
import com.healthcare.personal_health_monitoring.entity.LabReportFolder;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.LabReportFileRepository;
import com.healthcare.personal_health_monitoring.repository.LabReportFolderRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.FileUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${supabase.bucket.medical-files}")
    private String medicalFilesBucket;

    public LabContentsResponse getContents(Long patientId, Long folderId) {
        if (folderId != null) {
            getOwnedFolder(patientId, folderId);
        }

        List<LabReportFolder> folders = folderId == null
                ? folderRepo.findByPatientIdAndParentFolderIsNull(patientId)
                : folderRepo.findByPatientIdAndParentFolderId(patientId, folderId);

        List<LabReportFile> files = folderId == null
                ? fileRepo.findByPatientIdAndFolderIsNull(patientId)
                : fileRepo.findByPatientIdAndFolderId(patientId, folderId);

        LabContentsResponse response = new LabContentsResponse();
        response.setFolders(folders.stream().map(this::mapFolder).collect(Collectors.toList()));
        response.setFiles(files.stream().map(this::mapFile).collect(Collectors.toList()));
        response.setTotalFiles(fileRepo.countByPatientId(patientId));
        response.setTotalFolders(folderRepo.countByPatientId(patientId));
        return response;
    }

    @Transactional
    public LabFolderResponse createFolder(Long patientId, String name, Long parentFolderId) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (name == null || name.isBlank()) {
            throw new RuntimeException("Folder name is required");
        }

        LabReportFolder folder = new LabReportFolder();
        folder.setPatient(patient);
        folder.setName(name.trim());

        if (parentFolderId != null) {
            folder.setParentFolder(getOwnedFolder(patientId, parentFolderId));
        }

        folderRepo.save(folder);
        return mapFolder(folder);
    }

    @Transactional
    public LabFileResponse uploadFile(Long patientId, Long folderId, String title, MultipartFile file) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Lab report file is required");
        }

        LabReportFolder folder = folderId != null ? getOwnedFolder(patientId, folderId) : null;

        String objectPath = fileUploadService.uploadPrivateFile(
                file,
                medicalFilesBucket,
                buildStorageFolder(patientId, folder)
        );

        LabReportFile entity = new LabReportFile();
        entity.setPatient(patient);
        entity.setFolder(folder);
        entity.setTitle(title != null && !title.isBlank() ? title.trim() : "Untitled");
        entity.setOriginalName(file.getOriginalFilename());
        entity.setFileType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        entity.setFileUrl(objectPath);

        fileRepo.save(entity);
        return mapFile(entity);
    }

    @Transactional
    public void deleteFolder(Long patientId, Long folderId) {
        LabReportFolder folder = getOwnedFolder(patientId, folderId);

        List<LabReportFile> files = fileRepo.findByPatientIdAndFolderId(patientId, folderId);
        fileRepo.deleteAll(files);
        folderRepo.delete(folder);
    }

    @Transactional
    public void deleteFile(Long patientId, Long fileId) {
        LabReportFile file = fileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (file.getPatient().getId() != patientId) {
            throw new RuntimeException("Unauthorized");
        }

        fileRepo.delete(file);
    }

    private LabFolderResponse mapFolder(LabReportFolder folder) {
        LabFolderResponse response = new LabFolderResponse();
        response.setId(folder.getId());
        response.setName(folder.getName());
        response.setParentFolderId(folder.getParentFolder() != null ? folder.getParentFolder().getId() : null);
        response.setCreatedAt(folder.getCreatedAt());
        response.setFileCount(fileRepo.findByPatientIdAndFolderId(folder.getPatient().getId(), folder.getId()).size());
        return response;
    }

    private LabFileResponse mapFile(LabReportFile file) {
        LabFileResponse response = new LabFileResponse();
        response.setId(file.getId());
        response.setTitle(file.getTitle());
        response.setOriginalName(file.getOriginalName());
        response.setFileType(file.getFileType());
        response.setFileUrl(resolveAccessibleFileUrl(file.getFileUrl()));
        response.setFolderId(file.getFolder() != null ? file.getFolder().getId() : null);
        response.setUploadedAt(file.getUploadedAt());
        return response;
    }

    private LabReportFolder getOwnedFolder(Long patientId, Long folderId) {
        LabReportFolder folder = folderRepo.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (folder.getPatient().getId() != patientId) {
            throw new RuntimeException("Unauthorized");
        }

        return folder;
    }

    private String buildStorageFolder(Long patientId, LabReportFolder folder) {
        String baseFolder = "lab-reports/patient-" + patientId;
        if (folder == null) {
            return baseFolder + "/root";
        }
        return baseFolder + "/folder-" + folder.getId();
    }

    private String resolveAccessibleFileUrl(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            return storedPath;
        }
        if (storedPath.startsWith("http://") || storedPath.startsWith("https://")) {
            return storedPath;
        }
        return fileUploadService.createSignedUrl(medicalFilesBucket, storedPath, 3600);
    }
}
