package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.PeriodReminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodReminderRepository extends JpaRepository<PeriodReminder, Long> {

    List<PeriodReminder> findByActiveTrue();
}
