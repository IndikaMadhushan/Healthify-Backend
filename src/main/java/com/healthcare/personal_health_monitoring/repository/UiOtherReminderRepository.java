package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.UiOtherReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UiOtherReminderRepository
        extends JpaRepository<UiOtherReminder, Long> {

    List<UiOtherReminder> findByPatientId(Long patientId);
    List<UiOtherReminder> findAll();

}
