package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
    List<PatientAllergy> findByPatientId(Long patientId);
}
