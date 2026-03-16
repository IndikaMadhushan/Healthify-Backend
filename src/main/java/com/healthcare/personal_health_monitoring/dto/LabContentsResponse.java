// Single response object returned for any folder/root view
package com.healthcare.personal_health_monitoring.dto;

import lombok.Data;
import java.util.List;

@Data
public class LabContentsResponse {
    private List<LabFolderResponse> folders;
    private List<LabFileResponse> files;
    private long totalFiles;    // all files across ALL folders for this patient
    private long totalFolders;  // all folders for this patient
}