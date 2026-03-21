package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Admin;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserEmailHash(String emailHash);

    default Optional<Admin> findByUserEmail(String email) {
        String normalizedEmail = email == null ? null : email.trim();
        String emailHash = SensitiveDataSupport.blindIndex(normalizedEmail);

        if (emailHash != null) {
            Optional<Admin> indexedMatch = findByUserEmailHash(emailHash);
            if (indexedMatch.isPresent()) {
                return indexedMatch;
            }
        }

        if (normalizedEmail == null || normalizedEmail.isEmpty()) {
            return Optional.empty();
        }

        return findAll().stream()
                .filter(admin -> admin.getUser() != null
                        && admin.getUser().getEmail() != null
                        && admin.getUser().getEmail().equalsIgnoreCase(normalizedEmail))
                .findFirst();
    }
}
