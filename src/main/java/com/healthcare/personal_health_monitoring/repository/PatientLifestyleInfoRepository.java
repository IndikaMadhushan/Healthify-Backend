package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientLifestyleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientLifestyleInfoRepository extends JpaRepository<PatientLifestyleInfo, Long> {
}
