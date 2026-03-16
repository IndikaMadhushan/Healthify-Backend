package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.LabReportFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabReportFolderRepository extends JpaRepository<LabReportFolder, Long> {

    // root-level folders for a patient (parentFolder is null)
    List<LabReportFolder> findByPatientIdAndParentFolderIsNull(Long patientId);

    // sub-folders inside a specific parent folder
    List<LabReportFolder> findByPatientIdAndParentFolderId(Long patientId, Long parentFolderId);

    // count all folders belonging to a patient
    long countByPatientId(Long patientId);
}
