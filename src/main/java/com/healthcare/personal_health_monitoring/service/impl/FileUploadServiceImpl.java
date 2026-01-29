package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Path rootLocation = Paths.get("uploads");

    public FileUploadServiceImpl() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Cannot upload empty file!");
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path destinationFile = rootLocation.resolve(
                    Paths.get(fileName)).normalize().toAbsolutePath();

            Files.copy(
                    file.getInputStream(),
                    destinationFile,
                    StandardCopyOption.REPLACE_EXISTING
            );


            return "http://localhost:8080/uploads/" + fileName;  // URL to store in DB

        } catch (Exception e) {
            throw new RuntimeException("File upload failed!");
        }
    }
}

