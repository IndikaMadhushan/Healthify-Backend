package com.healthcare.personal_health_monitoring.repository;


import com.healthcare.personal_health_monitoring.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepo extends JpaRepository<Medication, Integer> {

    List<Medication> findAllByConsultPage_ConsultId(int consultId);
}
