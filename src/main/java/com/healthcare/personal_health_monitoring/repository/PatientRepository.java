package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;
import com.healthcare.personal_health_monitoring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserEmail(String email);
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByNic(String nic);

    Optional<Patient> findByPatientId(String patientId);
    //Optional<Patient> findById(Long patientId);
   // Patient<Patient> findByPatientId(String patientId);
    List<Patient> findByNicContainingIgnoreCase(String nic);
}
