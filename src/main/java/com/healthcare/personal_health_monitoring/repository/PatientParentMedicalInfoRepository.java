package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientParentMedicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientParentMedicalInfoRepository extends JpaRepository<PatientParentMedicalInfo, Long> {
}
