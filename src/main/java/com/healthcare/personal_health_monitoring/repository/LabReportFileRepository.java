
// ── LabReportFileRepository.java ─────────────────────────────────────────────
package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.LabReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabReportFileRepository extends JpaRepository<LabReportFile, Long> {

    // root-level files (no folder)
    List<LabReportFile> findByPatientIdAndFolderIsNull(Long patientId);

    // files inside a specific folder
    List<LabReportFile> findByPatientIdAndFolderId(Long patientId, Long folderId);

    // count all files belonging to a patient (used for total count badge)
    long countByPatientId(Long patientId);
}