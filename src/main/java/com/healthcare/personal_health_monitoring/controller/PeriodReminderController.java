package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.PeriodReminder;
import com.healthcare.personal_health_monitoring.service.PeriodReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reminders/period")
@RequiredArgsConstructor
public class PeriodReminderController {

    private final PeriodReminderService periodReminderService;

    @PostMapping("/{patientId}")
    public PeriodReminder setNextPeriod(
            @PathVariable Long patientId,
            @RequestParam String date) {

        return periodReminderService.setNextPeriodDate(
                patientId, LocalDate.parse(date));
    }

    @GetMapping("/{patientId}")
    public List<PeriodReminder> getHistory(@PathVariable Long patientId) {
        return periodReminderService.getHistory(patientId);
    }
}
