package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.MedicineReminderRequest;
import com.healthcare.personal_health_monitoring.dto.MedicineReminderResponse;
import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.entity.MedicineReminder;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.MedicineReminderRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.MedicineReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineReminderServiceImpl implements MedicineReminderService {

    private final MedicineReminderRepository reminderRepository;
    private final PatientRepository patientRepository;

    @Override
    public MedicineReminderResponse addReminder(Long patientId, MedicineReminderRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow();

        MedicineReminder reminder = new MedicineReminder();

        reminder.setPatient(patient);
        reminder.setMedicineName(request.getMedicineName());
        reminder.setReminderType(request.getReminderType());
        reminder.setSpecificDate(request.getSpecificDate());
        reminder.setTime(request.getTime());
        reminder.setDaysOfWeek(request.getDaysOfWeek());
        reminder.setHourlyInterval(request.getHourlyInterval());
        reminder.setActive(true);
        reminder.setLastTriggeredAt(LocalDateTime.now());

        MedicineReminder saved = reminderRepository.save(reminder);

        return new MedicineReminderResponse(
                saved.getId(),
                saved.getMedicineName(),
                saved.getReminderType().name(),
                saved.getTime(),
                saved.isActive(),
                saved.getPatient().getPatientId()
        );
    }

    @Override
    public List<MedicineReminderResponse> getPatientReminders(Long patientId) {

        List<MedicineReminder> reminders = reminderRepository.findByPatientIdAndActiveTrue(patientId);

        return reminders.stream().map(r -> new MedicineReminderResponse(
                r.getId(),
                r.getMedicineName(),
                r.getReminderType().name(),
                r.getTime(),
                r.isActive(),
                r.getPatient().getPatientId()
        )).toList();
    }



    @Override
    public void deactivateReminder(Long reminderId) {
        MedicineReminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        reminder.setActive(false);
        reminderRepository.save(reminder);

    }
}
