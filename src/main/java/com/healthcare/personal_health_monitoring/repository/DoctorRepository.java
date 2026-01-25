package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserEmail(String email);
    Optional<Doctor> findByNic(String nic);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    Optional<Doctor> findByDoctorId(String doctorId);
    List<Doctor> findByUserEnabled(boolean enabled);
}
