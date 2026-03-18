package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientVaccineSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientVaccineSelectionRepository extends JpaRepository<PatientVaccineSelection, Long> {
    List<PatientVaccineSelection> findByPatientIdOrderByIdAsc(Long patientId);
    void deleteByPatientId(Long patientId);
}
