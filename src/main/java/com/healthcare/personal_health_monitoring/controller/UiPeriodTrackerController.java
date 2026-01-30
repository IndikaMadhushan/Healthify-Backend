package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.PeriodReminderRequest;
import com.healthcare.personal_health_monitoring.dto.PeriodReminderResponse;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.UiPeriodTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ui/reminders/period")
@RequiredArgsConstructor
public class UiPeriodTrackerController {

    private final UiPeriodTrackerService service;

    @GetMapping
    public PeriodReminderResponse getActive(
            @AuthenticationPrincipal CustomUserDetails principal) {

        return service.getActive(principal.getUser());
    }

    @PostMapping
    public PeriodReminderResponse saveOrUpdate(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody PeriodReminderRequest request) {

        return service.saveOrUpdate(principal.getUser(), request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deactivate(id);
    }
}


