package com.healthcare.personal_health_monitoring.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LabFileResponse {
    private Long id;
    private String title;
    private String originalName;
    private String fileType;
    private String fileUrl;
    private Long folderId;         // null if root
    private LocalDateTime uploadedAt;
}