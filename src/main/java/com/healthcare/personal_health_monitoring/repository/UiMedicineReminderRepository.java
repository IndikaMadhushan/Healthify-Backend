package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.UiMedicineReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UiMedicineReminderRepository
        extends JpaRepository<UiMedicineReminder, Long> {

    List<UiMedicineReminder> findByPatientId(Long patientId);
    List<UiMedicineReminder> findAll();

}
