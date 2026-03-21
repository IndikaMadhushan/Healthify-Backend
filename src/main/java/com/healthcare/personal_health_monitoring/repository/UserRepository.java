package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailHash(String emailHash);
    List<User> findByRoleAndEnabled(UserRole role, boolean enabled);

    default Optional<User> findByEmail(String email) {
        String normalizedEmail = email == null ? null : email.trim();
        String emailHash = SensitiveDataSupport.blindIndex(normalizedEmail);

        if (emailHash != null) {
            Optional<User> indexedMatch = findByEmailHash(emailHash);
            if (indexedMatch.isPresent()) {
                return indexedMatch;
            }
        }

        if (normalizedEmail == null || normalizedEmail.isEmpty()) {
            return Optional.empty();
        }

        return findAll().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(normalizedEmail))
                .findFirst();
    }

}
