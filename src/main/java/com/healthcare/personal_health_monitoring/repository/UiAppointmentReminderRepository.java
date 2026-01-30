package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.UiAppointmentReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UiAppointmentReminderRepository
        extends JpaRepository<UiAppointmentReminder, Long> {

    List<UiAppointmentReminder> findByPatientId(Long patientId);
    List<UiAppointmentReminder> findByCompletedFalse();

}
