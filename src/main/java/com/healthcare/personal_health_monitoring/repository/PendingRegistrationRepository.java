package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PendingRegistration;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {

    Optional<PendingRegistration> findByEmailHash(String emailHash);
    boolean existsByEmailHash(String emailHash);
    boolean existsByNicHash(String nicHash);
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByNicHashAndEmailHashNot(String nicHash, String emailHash);
    boolean existsByLicenseNumberAndEmailHashNot(String licenseNumber, String emailHash);

    default Optional<PendingRegistration> findByEmail(String email) {
        String normalizedEmail = email == null ? null : email.trim();
        String emailHash = SensitiveDataSupport.blindIndex(normalizedEmail);

        if (emailHash != null) {
            Optional<PendingRegistration> indexedMatch = findByEmailHash(emailHash);
            if (indexedMatch.isPresent()) {
                return indexedMatch;
            }
        }

        if (normalizedEmail == null || normalizedEmail.isEmpty()) {
            return Optional.empty();
        }

        return findAll().stream()
                .filter(registration -> registration.getEmail() != null
                        && registration.getEmail().equalsIgnoreCase(normalizedEmail))
                .findFirst();
    }

    default boolean existsByEmail(String email) {
        String emailHash = SensitiveDataSupport.blindIndex(email);
        return (emailHash != null && existsByEmailHash(emailHash))
                || findByEmail(email).isPresent();
    }

    default boolean existsByNic(String nic) {
        String nicHash = SensitiveDataSupport.blindIndex(nic);
        if (nicHash != null && existsByNicHash(nicHash)) {
            return true;
        }

        String normalizedNic = nic == null ? null : nic.trim();
        if (normalizedNic == null || normalizedNic.isEmpty()) {
            return false;
        }

        return findAll().stream()
                .anyMatch(registration -> registration.getNic() != null
                        && registration.getNic().equalsIgnoreCase(normalizedNic));
    }

    default boolean existsByNicAndEmailNot(String nic, String email) {
        String nicHash = SensitiveDataSupport.blindIndex(nic);
        String emailHash = SensitiveDataSupport.blindIndex(email);
        if (nicHash != null && emailHash != null && existsByNicHashAndEmailHashNot(nicHash, emailHash)) {
            return true;
        }

        String normalizedNic = nic == null ? null : nic.trim();
        String normalizedEmail = email == null ? null : email.trim();
        if (normalizedNic == null || normalizedNic.isEmpty()) {
            return false;
        }

        return findAll().stream()
                .anyMatch(registration ->
                        registration.getNic() != null
                                && registration.getNic().equalsIgnoreCase(normalizedNic)
                                && (normalizedEmail == null
                                || registration.getEmail() == null
                                || !registration.getEmail().equalsIgnoreCase(normalizedEmail))
                );
    }

    default boolean existsByLicenseNumberAndEmailNot(String licenseNumber, String email) {
        String emailHash = SensitiveDataSupport.blindIndex(email);
        if (emailHash != null && existsByLicenseNumberAndEmailHashNot(licenseNumber, emailHash)) {
            return true;
        }

        String normalizedEmail = email == null ? null : email.trim();
        return findAll().stream()
                .anyMatch(registration ->
                        registration.getLicenseNumber() != null
                                && registration.getLicenseNumber().equalsIgnoreCase(licenseNumber)
                                && (normalizedEmail == null
                                || registration.getEmail() == null
                                || !registration.getEmail().equalsIgnoreCase(normalizedEmail))
                );
    }
}
