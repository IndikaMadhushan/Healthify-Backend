package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientDiseaseRepository extends JpaRepository<PatientDisease, Long> {
    List<PatientDisease> findByPatientId(Long patientId);
}
