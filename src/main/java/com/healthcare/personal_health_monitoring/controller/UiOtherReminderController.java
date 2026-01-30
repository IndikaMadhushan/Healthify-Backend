package com.healthcare.personal_health_monitoring.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.healthcare.personal_health_monitoring.dto.UiOtherReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiOtherReminderResponse;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.UiOtherReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ui/reminders/others")
@RequiredArgsConstructor
public class UiOtherReminderController {

    private final UiOtherReminderService service;

    @PostMapping
    public UiOtherReminderResponse create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody UiOtherReminderRequest req) {

        return service.create(principal.getUser(), req);
    }


    @GetMapping
    public List<UiOtherReminderResponse> getAll(
            @AuthenticationPrincipal CustomUserDetails principal) {

        return service.getAll(principal.getUser());
    }



    @PutMapping("/{id}")
    public UiOtherReminderResponse update(
            @PathVariable Long id,
            @RequestBody UiOtherReminderRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
