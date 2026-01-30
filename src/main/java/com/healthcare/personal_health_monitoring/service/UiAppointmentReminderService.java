package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.UiAppointmentReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiAppointmentReminderResponse;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.UiAppointmentReminder;
import com.healthcare.personal_health_monitoring.entity.User;

import java.util.List;

public interface UiAppointmentReminderService {
     Patient p(User u);

    UiAppointmentReminderResponse create(User u, UiAppointmentReminderRequest r);

    List<UiAppointmentReminderResponse> getAll(User u);

    UiAppointmentReminderResponse update(Long id, UiAppointmentReminderRequest r);

     void delete(Long id);

    UiAppointmentReminderResponse map(UiAppointmentReminder a);
}
