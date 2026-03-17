package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientMedicalInfoProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientMedicalInfoProfileRepository extends JpaRepository<PatientMedicalInfoProfile, Long> {
}
