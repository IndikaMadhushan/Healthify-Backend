package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileAccessController {

    private final FileUploadService fileUploadService;

    @GetMapping("/signed-url")
    public ResponseEntity<String> getSignedUrl(
            @RequestParam String bucket,
            @RequestParam String path
    ) {
        String signedUrl = fileUploadService.createSignedUrl(bucket, path, 300);
        return ResponseEntity.ok(signedUrl);
    }
}
