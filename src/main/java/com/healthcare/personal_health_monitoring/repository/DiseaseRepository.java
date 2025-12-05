package com.healthcare.personal_health_monitoring.repository;


import com.healthcare.personal_health_monitoring.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    Optional<Disease> findByNameIgnoreCase(String name);
}
