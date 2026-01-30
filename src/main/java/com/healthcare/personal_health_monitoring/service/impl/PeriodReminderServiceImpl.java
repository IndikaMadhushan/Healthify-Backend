//package com.healthcare.personal_health_monitoring.service.impl;
//
//import com.healthcare.personal_health_monitoring.entity.*;
//import com.healthcare.personal_health_monitoring.repository.*;
//import com.healthcare.personal_health_monitoring.service.PeriodReminderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PeriodReminderServiceImpl implements PeriodReminderService {
//
//    private final PeriodTrackerRepository periodTrackerRepository;
//    private final PatientRepository patientRepository;
//
//    @Override
//    public PeriodReminder setNextPeriodDate(Long patientId, LocalDate lastDate) {
//
//        Patient patient = patientRepository.findById(patientId).orElseThrow();
//
//        PeriodReminder reminder = new PeriodReminder();
//        reminder.setPatient(patient);
//        reminder.setLastPeriodDate(lastDate);
//
//        return periodTrackerRepository.save(reminder);
//    }
//
//    @Override
//    public List<PeriodReminder> getHistory(Long patientId) {
//        return periodTrackerRepository.findByActiveTrue().stream()
//                .filter(r -> r.getPatient().getId() == patientId)
//                .toList();
//    }
//}
