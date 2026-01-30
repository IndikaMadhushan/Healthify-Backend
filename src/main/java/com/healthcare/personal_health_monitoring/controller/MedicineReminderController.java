//package com.healthcare.personal_health_monitoring.controller;
//
//import com.healthcare.personal_health_monitoring.dto.MedicineReminderRequest;
//import com.healthcare.personal_health_monitoring.dto.MedicineReminderResponse;
//import com.healthcare.personal_health_monitoring.entity.MedicineReminder;
//import com.healthcare.personal_health_monitoring.service.MedicineReminderService;
//import jakarta.persistence.SqlResultSetMapping;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/reminders")
//@RequiredArgsConstructor
//public class MedicineReminderController {
//    private final MedicineReminderService reminderService;
//
//    //Add reminder
//    @PostMapping("/{patientId}")
//    public MedicineReminderResponse addReminder(
//            @PathVariable Long patientId,
//            @RequestBody MedicineReminderRequest request
//            ){
//        return reminderService.addReminder(patientId, request);
//    }
//
//    //view reminders
//    @GetMapping("/{patientId}")
//    public List<MedicineReminderResponse> getReminders(@PathVariable Long patientId){
//        return reminderService.getPatientReminders(patientId);
//
//    }
//
//    //disable reminder
//    @PutMapping("{reminderId}")
//    public String deactive(@PathVariable Long reminderId) {
//        reminderService.deactivateReminder(reminderId);
//        return "Reminder deactivated successfully";
//    }
//
//}
