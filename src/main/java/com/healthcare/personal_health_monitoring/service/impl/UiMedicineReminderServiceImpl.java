package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.UiMedicineReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiMedicineReminderResponse;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.UiMedicineReminder;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.UiMedicineReminderRepository;
import com.healthcare.personal_health_monitoring.service.UiMedicineReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UiMedicineReminderServiceImpl implements UiMedicineReminderService {

    private final UiMedicineReminderRepository repository;
    private final PatientRepository patientRepository;

    public UiMedicineReminderResponse create(
            User user,
            UiMedicineReminderRequest req) {

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        UiMedicineReminder reminder = new UiMedicineReminder();
        reminder.setPatient(patient);
        reminder.setMedicineName(req.getMedicineName());
        reminder.setDosage(req.getDosage());
        reminder.setFrequency(req.getFrequency());
        reminder.setTime(req.getTime());
        reminder.setDuration(req.getDuration());
        reminder.setNotes(req.getNotes());

        UiMedicineReminder saved = repository.save(reminder);

        return map(saved);
    }

    public List<UiMedicineReminderResponse> getAll(User user) {
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repository.findByPatientId(patient.getId())
                .stream().map(this::map).toList();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public UiMedicineReminderResponse map(UiMedicineReminder r) {
        return new UiMedicineReminderResponse(
                r.getId(),
                r.getMedicineName(),
                r.getDosage(),
                r.getFrequency(),
                r.getTime(),
                r.getDuration(),
                r.getNotes()
        );
    }

    public UiMedicineReminderResponse update(Long id, UiMedicineReminderRequest req) {
        UiMedicineReminder reminder = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine reminder not found"));

        reminder.setMedicineName(req.getMedicineName());
        reminder.setDosage(req.getDosage());
        reminder.setFrequency(req.getFrequency());
        reminder.setTime(req.getTime());
        reminder.setDuration(req.getDuration());
        reminder.setNotes(req.getNotes());

        return map(repository.save(reminder));
    }
}
