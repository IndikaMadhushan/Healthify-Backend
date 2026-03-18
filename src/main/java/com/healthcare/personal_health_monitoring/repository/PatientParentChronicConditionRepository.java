package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientParentChronicCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientParentChronicConditionRepository extends JpaRepository<PatientParentChronicCondition, Long> {
    List<PatientParentChronicCondition> findByPatientIdOrderByIdAsc(Long patientId);
    void deleteByPatientId(Long patientId);
}
