package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserEmailHash(String emailHash);
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByNicHash(String nicHash);

    Optional<Patient> findByPatientId(String patientId);
    //Optional<Patient> findById(Long patientId);
   // Patient<Patient> findByPatientId(String patientId);
    Optional<Patient> findByUserId(long userId);

    default Optional<Patient> findByUserEmail(String email) {
        String normalizedEmail = email == null ? null : email.trim();
        String emailHash = SensitiveDataSupport.blindIndex(normalizedEmail);

        if (emailHash != null) {
            Optional<Patient> indexedMatch = findByUserEmailHash(emailHash);
            if (indexedMatch.isPresent()) {
                return indexedMatch;
            }
        }

        if (normalizedEmail == null || normalizedEmail.isEmpty()) {
            return Optional.empty();
        }

        return findAll().stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equalsIgnoreCase(normalizedEmail))
                .findFirst();
    }

    default Optional<Patient> findByNic(String nic) {
        String normalizedNic = nic == null ? null : nic.trim();
        String nicHash = SensitiveDataSupport.blindIndex(normalizedNic);

        if (nicHash != null) {
            Optional<Patient> indexedMatch = findByNicHash(nicHash);
            if (indexedMatch.isPresent()) {
                return indexedMatch;
            }
        }

        if (normalizedNic == null || normalizedNic.isEmpty()) {
            return Optional.empty();
        }

        return findAll().stream()
                .filter(patient -> patient.getNic() != null && patient.getNic().equalsIgnoreCase(normalizedNic))
                .findFirst();
    }

    default List<Patient> findByNicContainingIgnoreCase(String nic) {
        String normalizedNic = nic == null ? "" : nic.trim().toLowerCase();
        if (normalizedNic.isEmpty()) {
            return List.of();
        }

        return findAll().stream()
                .filter(patient -> patient.getNic() != null
                        && patient.getNic().toLowerCase().contains(normalizedNic))
                .toList();
    }


}
