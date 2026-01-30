//package com.healthcare.personal_health_monitoring.service.impl;
//
//import com.healthcare.personal_health_monitoring.dto.OtherReminderRequest;
//import com.healthcare.personal_health_monitoring.dto.OtherReminderResponse;
//import com.healthcare.personal_health_monitoring.entity.OtherReminder;
//import com.healthcare.personal_health_monitoring.entity.Patient;
//import com.healthcare.personal_health_monitoring.repository.OtherReminderRepository;
//import com.healthcare.personal_health_monitoring.repository.PatientRepository;
//import com.healthcare.personal_health_monitoring.service.OtherReminderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OtherReminderServiceImpl implements OtherReminderService {
//
//    private final OtherReminderRepository otherReminderRepository;
//    private final PatientRepository patientRepository;
//
//    @Override
//    public OtherReminderResponse addReminder(
//            Long patientId,
//            OtherReminderRequest request
//        ) {
//        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
//
//        OtherReminder reminder = new OtherReminder();
//        reminder.setPatient(patient);
//        reminder.setReminderType(request.getReminderType());
//        reminder.setSpecificDate(request.getSpecificDate());
//        reminder.setTime(request.getTime());
//        reminder.setDaysOfWeek(request.getDaysOfWeek());
//        reminder.setNote(request.getNote());
//
//        OtherReminder saved = otherReminderRepository.save(reminder);
//
//        return new  OtherReminderResponse(
//                saved.getId(),
//                saved.getReminderType(),
//                saved.getTime(),
//                saved.getNote()
//        );
//
//    }
//
//    @Override
//    public List<OtherReminderResponse> getReminders(Long patientId) {
//        return otherReminderRepository.findByActiveTrue().stream()
//                .map(r -> new OtherReminderResponse(
//                        r.getId(),
//                        r.getReminderType(),
//                        r.getTime(),
//                        r.getNote()
//                )).toList();
//    }
//
//    @Override
//    public void deactivateReminder(Long reminderId) {
//        OtherReminder reminder = otherReminderRepository.findById(reminderId).orElseThrow(() -> new RuntimeException("No such reminder"));
//        reminder.setActive(false);
//        otherReminderRepository.save(reminder);
//
//    }
//}
