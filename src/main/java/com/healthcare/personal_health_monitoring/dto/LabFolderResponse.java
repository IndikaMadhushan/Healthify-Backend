

package com.healthcare.personal_health_monitoring.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LabFolderResponse {
    private Long id;
    private String name;
    private Long parentFolderId;   // null if root
    private LocalDateTime createdAt;
    private long fileCount;        // files directly inside this folder
}
