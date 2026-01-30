//package com.healthcare.personal_health_monitoring.controller;
//
//import com.healthcare.personal_health_monitoring.dto.PeriodReminderRequest;
//import com.healthcare.personal_health_monitoring.dto.PeriodReminderResponse;
//import com.healthcare.personal_health_monitoring.service.PeriodReminderService;
//import com.healthcare.personal_health_monitoring.service.UiPeriodReminderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/reminders/period")
//@RequiredArgsConstructor
//public class PeriodReminderController {
//
//    private final PeriodReminderService periodService;
//
//    @PostMapping("/{patientId}")
//    public PeriodReminderResponse saveOrUpdate(
//            @PathVariable Long patientId,
//            @RequestBody PeriodReminderRequest request
//    ) {
//        return periodService.saveOrUpdate(patientId, request);
//    }
//
//    @GetMapping("/{patientId}")
//    public PeriodReminderResponse getActive(@PathVariable Long patientId) {
//        return periodService.getActiveTracker(patientId);
//    }
//
//    @DeleteMapping("/{trackerId}")
//    public void deactivate(@PathVariable Long trackerId) {
//        periodService.deactivate(trackerId);
//    }
//}
