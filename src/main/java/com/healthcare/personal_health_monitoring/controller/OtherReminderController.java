package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.dto.OtherReminderRequest;
import com.healthcare.personal_health_monitoring.dto.OtherReminderResponse;
import com.healthcare.personal_health_monitoring.service.OtherReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders/other")
@RequiredArgsConstructor
public class OtherReminderController {

    private final OtherReminderService otherReminderService;

    @PostMapping("/{patientId}")
    public OtherReminderResponse addReminder(
            @PathVariable Long patientId,
            @RequestBody OtherReminderRequest request
            ){
        return otherReminderService.addReminder(patientId, request);
    }

    @GetMapping("/{patientId}")
    public List<OtherReminderResponse> getReminders(@PathVariable Long patientId) {
        return otherReminderService.getReminders(patientId);
    }

    @PutMapping("/{reminderId}/deactivate")
    public String  deactivate(@PathVariable Long remindeId){
        otherReminderService.deactivateReminder(remindeId);
        return "reminder deactivated";
    }

}
