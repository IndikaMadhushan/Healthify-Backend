package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.MedicineReminderRequest;
import com.healthcare.personal_health_monitoring.entity.MedicineReminder;

import java.util.List;

public interface MedicineReminderService {
    MedicineReminder addReminder(Long patientId, MedicineReminderRequest request);

    List<MedicineReminder> getPatientReminders(Long patientId);

    void deactivateReminder(Long reminderId);
}
