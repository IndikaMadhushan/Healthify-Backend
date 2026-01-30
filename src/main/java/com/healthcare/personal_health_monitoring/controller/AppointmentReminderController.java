//package com.healthcare.personal_health_monitoring.controller;
//
//import com.healthcare.personal_health_monitoring.dto.AppointmentReminderRequest;
//import com.healthcare.personal_health_monitoring.dto.AppointmentReminderResponse;
//import com.healthcare.personal_health_monitoring.service.AppointmentReminderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/appointments")
//@RequiredArgsConstructor
//public class AppointmentReminderController {
//
//    private final AppointmentReminderService appointmentService;
//
//    @PostMapping("/{patientId}")
//    public ResponseEntity<AppointmentReminderResponse> addAppointment(
//            @PathVariable Long patientId,
//            @RequestBody AppointmentReminderRequest request
//            ) {
//        return ResponseEntity.ok(appointmentService.addAppointment(patientId, request));
//    }
//
//    @GetMapping("/{patientId}")
//    public ResponseEntity<List<AppointmentReminderResponse>> getAppointments(
//            @PathVariable Long patientId
//            ) {
//        return ResponseEntity.ok(appointmentService.getAppointment(patientId));
//    }
//
//    @PutMapping("/{appointmentId}/complete")
//    public ResponseEntity<String> markComplete(@PathVariable Long appointmentId){
//        appointmentService.markCompleted(appointmentId);
//        return ResponseEntity.ok("Appointment marked as completed");
//    }
//
//}
