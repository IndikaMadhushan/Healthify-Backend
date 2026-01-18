package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.OtherReminderRequest;
import com.healthcare.personal_health_monitoring.dto.OtherReminderResponse;

import java.util.List;

public interface OtherReminderService {
    OtherReminderResponse addReminder(Long patientId, OtherReminderRequest request);

    List<OtherReminderResponse> getReminders(Long patientId);

    void deactivateReminder(Long reminderId);

}
