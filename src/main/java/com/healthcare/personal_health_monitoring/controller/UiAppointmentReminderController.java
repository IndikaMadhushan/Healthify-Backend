package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.UiAppointmentReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiAppointmentReminderResponse;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.UiAppointmentReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ui/reminders/appointments")
@RequiredArgsConstructor
public class UiAppointmentReminderController {

    private final UiAppointmentReminderService service;

    @PostMapping
    public UiAppointmentReminderResponse create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody UiAppointmentReminderRequest req) {

        return service.create(principal.getUser(), req);
    }

    @GetMapping
    public List<UiAppointmentReminderResponse> getAll(
            @AuthenticationPrincipal CustomUserDetails principal) {

        return service.getAll(principal.getUser());
    }

    @PutMapping("/{id}")
    public UiAppointmentReminderResponse update(
            @PathVariable Long id,
            @RequestBody UiAppointmentReminderRequest req) {

        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}