package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.PeriodReminderRequest;
import com.healthcare.personal_health_monitoring.dto.PeriodReminderResponse;
import com.healthcare.personal_health_monitoring.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UiPeriodTrackerService {

    PeriodReminderResponse saveOrUpdate(User user, PeriodReminderRequest request);

    PeriodReminderResponse getActive(User user);

    void deactivate(Long trackerId);
}
