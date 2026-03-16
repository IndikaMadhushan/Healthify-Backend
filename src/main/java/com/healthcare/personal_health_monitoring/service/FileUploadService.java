package com.healthcare.personal_health_monitoring.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadPublicProfileImage(MultipartFile file, String folder);

    String uploadPrivateFile(MultipartFile file, String bucket, String folder);

    String createSignedUrl(String bucket, String objectPath, int expiresInSeconds);

    byte[] downloadPrivateFile(String bucket, String objectPath);
}

