package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    List<Vaccination> findByPatientId(Long patientId);
}
