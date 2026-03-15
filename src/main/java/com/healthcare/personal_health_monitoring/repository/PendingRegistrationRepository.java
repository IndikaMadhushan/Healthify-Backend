package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {
    Optional<PendingRegistration> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNic(String nic);
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByNicAndEmailNot(String nic, String email);
    boolean existsByLicenseNumberAndEmailNot(String licenseNumber, String email);
}
