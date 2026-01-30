package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.UiOtherReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiOtherReminderResponse;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.UiOtherReminder;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.UiOtherReminderRepository;
import com.healthcare.personal_health_monitoring.service.UiOtherReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UiOtherReminderServiceImpl implements UiOtherReminderService {

    private final UiOtherReminderRepository repository;
    private final PatientRepository patientRepository;

    private Patient patient(User user) {
        if (user == null) {
            throw new RuntimeException("Unauthenticated user");
        }

        return patientRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found for user: " + user.getEmail())
                );
    }


    @Override
    public UiOtherReminderResponse create(User user, UiOtherReminderRequest req) {
        UiOtherReminder reminder = new UiOtherReminder();
        reminder.setPatient(patient(user));
        reminder.setTitle(req.getTitle());
        reminder.setCategory(req.getCategory());
        reminder.setReminderDate(req.getReminderDate());
        reminder.setTime(req.getTime());
        reminder.setDescription(req.getDescription());
        reminder.setIcon(req.getIcon());

        return map(repository.save(reminder));
    }

    @Override
    public List<UiOtherReminderResponse> getAll(User user) {
        return repository.findByPatientId(patient(user).getId())
                .stream().map(this::map).toList();
    }

    @Override
    public UiOtherReminderResponse update(Long id, UiOtherReminderRequest req) {
        UiOtherReminder reminder = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
        reminder.setTitle(req.getTitle());
        reminder.setCategory(req.getCategory());
        reminder.setReminderDate(req.getReminderDate());
        reminder.setTime(req.getTime());
        reminder.setDescription(req.getDescription());
        reminder.setIcon(req.getIcon());
        return map(repository.save(reminder));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UiOtherReminderResponse map(UiOtherReminder reminder) {
        return new UiOtherReminderResponse(
                reminder.getId(),
                reminder.getTitle(),
                reminder.getCategory(),
                reminder.getReminderDate(),
                reminder.getTime(),
                reminder.getDescription(),
                reminder.getIcon()
        );
    }
}
