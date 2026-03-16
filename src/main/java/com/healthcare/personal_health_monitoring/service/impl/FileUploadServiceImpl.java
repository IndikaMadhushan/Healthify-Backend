package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.bucket.profile-images}")
    private String profileImagesBucket;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String uploadPublicProfileImage(MultipartFile file, String folder) {
        String objectPath = buildObjectPath(folder, file.getOriginalFilename());
        uploadToSupabase(profileImagesBucket, objectPath, file);

        String encodedPath = encodePath(objectPath);
        return supabaseUrl + "/storage/v1/object/public/" + profileImagesBucket + "/" + encodedPath;
    }

    @Override
    public String uploadPrivateFile(MultipartFile file, String bucket, String folder) {
        String objectPath = buildObjectPath(folder, file.getOriginalFilename());
        uploadToSupabase(bucket, objectPath, file);
        return objectPath;
    }

    @Override
    public String createSignedUrl(String bucket, String objectPath, int expiresInSeconds) {
        String url = supabaseUrl + "/storage/v1/object/sign/" + bucket;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", serviceKey);
        headers.setBearerAuth(serviceKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "expiresIn", expiresInSeconds,
                "path", objectPath
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

        if (response == null || response.get("signedURL") == null) {
            throw new RuntimeException("Failed to create signed URL");
        }

        return supabaseUrl + "/storage/v1" + response.get("signedURL").toString();
    }

    @Override
    public byte[] downloadPrivateFile(String bucket, String objectPath) {
        String url = supabaseUrl + "/storage/v1/object/authenticated/" + bucket + "/" + encodePath(objectPath);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", serviceKey);
        headers.setBearerAuth(serviceKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                byte[].class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to download private file");
        }

        return response.getBody();
    }

    private void uploadToSupabase(String bucket, String objectPath, MultipartFile file) {
        try {
            String url = supabaseUrl + "/storage/v1/object/" + bucket + "/" + encodePath(objectPath);

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", serviceKey);
            headers.setBearerAuth(serviceKey);
            headers.setContentType(MediaType.parseMediaType(
                    file.getContentType() != null ? file.getContentType() : "application/octet-stream"
            ));
            headers.set("x-upsert", "false");

            byte[] bytes = file.getBytes();
            HttpEntity<byte[]> request = new HttpEntity<>(bytes, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Supabase upload failed: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    private String buildObjectPath(String folder, String originalFilename) {
        String safeFolder = sanitizePathSegment(folder, "misc");
        String safeName = sanitizePathSegment(originalFilename, "file");
        return safeFolder + "/" + UUID.randomUUID() + "_" + safeName;
    }

    private String sanitizePathSegment(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return fallback;
        }
        String normalized = trimmed.replace("\\", "/");
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        String cleaned = normalized.replaceAll("[^a-zA-Z0-9/_\\.-]", "_");
        return cleaned.isEmpty() ? fallback : cleaned;
    }

    private String encodePath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8).replace("+", "%20").replace("%2F", "/");
    }
}

