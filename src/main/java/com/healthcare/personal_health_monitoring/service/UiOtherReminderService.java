package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.UiOtherReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiOtherReminderResponse;
import com.healthcare.personal_health_monitoring.entity.UiOtherReminder;
import com.healthcare.personal_health_monitoring.entity.User;

import java.util.List;

public interface UiOtherReminderService {
    UiOtherReminderResponse create(User user, UiOtherReminderRequest req);

    List<UiOtherReminderResponse> getAll(User user);

    UiOtherReminderResponse update(Long id, UiOtherReminderRequest req);

    void delete(Long id);

    UiOtherReminderResponse map(UiOtherReminder reminder);
}
