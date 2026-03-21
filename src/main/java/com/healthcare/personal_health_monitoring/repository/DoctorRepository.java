package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserEmailHash(String emailHash);
    Optional<Doctor> findByNic(String nic);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    Optional<Doctor> findByDoctorId(String doctorId);
    List<Doctor> findByUserEnabled(boolean enabled);
    Optional<Doctor> findById(long id);

    default Optional<Doctor> findByUserEmail(String email) {
        String normalizedEmail = email == null ? null : email.trim();
        String emailHash = SensitiveDataSupport.blindIndex(normalizedEmail);

        if (emailHash != null) {
            Optional<Doctor> indexedMatch = findByUserEmailHash(emailHash);
            if (indexedMatch.isPresent()) {
                return indexedMatch;
            }
        }

        if (normalizedEmail == null || normalizedEmail.isEmpty()) {
            return Optional.empty();
        }

        return findAll().stream()
                .filter(doctor -> doctor.getEmail() != null && doctor.getEmail().equalsIgnoreCase(normalizedEmail))
                .findFirst();
    }
}
