package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientChronicCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientChronicConditionRepository extends JpaRepository<PatientChronicCondition, Long> {
    List<PatientChronicCondition> findByPatientIdOrderByIdAsc(Long patientId);
    void deleteByPatientId(Long patientId);
}
