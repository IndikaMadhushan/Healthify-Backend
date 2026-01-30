package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.UiMedicineReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiMedicineReminderResponse;
import com.healthcare.personal_health_monitoring.entity.UiMedicineReminder;
import com.healthcare.personal_health_monitoring.entity.User;

import java.util.List;

public interface UiMedicineReminderService {
     UiMedicineReminderResponse create(
            User user,
            UiMedicineReminderRequest req);

     List<UiMedicineReminderResponse> getAll(User user);

    UiMedicineReminderResponse update(Long id, UiMedicineReminderRequest req);


    void delete(Long id);

    UiMedicineReminderResponse map(UiMedicineReminder r);
}
