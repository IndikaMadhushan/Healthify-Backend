package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.UiMedicineReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiMedicineReminderResponse;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.security.CustomUserDetails;
import com.healthcare.personal_health_monitoring.service.UiMedicineReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ui/reminders/medicines")
@RequiredArgsConstructor
public class UiMedicineReminderController {

    private final UiMedicineReminderService service;

    @PostMapping
    public UiMedicineReminderResponse create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody UiMedicineReminderRequest req) {

        return service.create(principal.getUser(), req);
    }

    @GetMapping
    public List<UiMedicineReminderResponse> getAll(
            @AuthenticationPrincipal CustomUserDetails principal) {

        return service.getAll(principal.getUser());
    }

    @PutMapping("/{id}")
    public UiMedicineReminderResponse update(
            @PathVariable Long id,
            @RequestBody UiMedicineReminderRequest req) {

        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

