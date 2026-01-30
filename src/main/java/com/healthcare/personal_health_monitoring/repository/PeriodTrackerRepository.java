package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PeriodTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeriodTrackerRepository extends JpaRepository<PeriodTracker, Long> {

    Optional<PeriodTracker> findByPatientIdAndActiveTrue(Long patientId);
    List<PeriodTracker> findByActiveTrue();
}
