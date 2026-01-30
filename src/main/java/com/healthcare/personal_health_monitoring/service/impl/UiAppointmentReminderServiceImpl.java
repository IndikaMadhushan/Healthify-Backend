package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.UiAppointmentReminderRequest;
import com.healthcare.personal_health_monitoring.dto.UiAppointmentReminderResponse;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.UiAppointmentReminder;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.UiAppointmentReminderRepository;
import com.healthcare.personal_health_monitoring.service.UiAppointmentReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UiAppointmentReminderServiceImpl implements UiAppointmentReminderService {

    private final UiAppointmentReminderRepository repo;
    private final PatientRepository patientRepo;

    public Patient p(User u){
        return patientRepo.findByUser(u)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public UiAppointmentReminderResponse create(User u, UiAppointmentReminderRequest r){
        UiAppointmentReminder a = new UiAppointmentReminder();
        a.setPatient(p(u));
        a.setTitle(r.getTitle());
        a.setAppointmentDate(r.getAppointmentDate());
        a.setTime(r.getTime());
        a.setLocation(r.getLocation());
        a.setDoctor(r.getDoctor());
        a.setReason(r.getReason());
        return map(repo.save(a));
    }

    public List<UiAppointmentReminderResponse> getAll(User u){
        return repo.findByPatientId(p(u).getId())
                .stream().map(this::map).toList();
    }

    public UiAppointmentReminderResponse update(Long id, UiAppointmentReminderRequest r){
        UiAppointmentReminder a = repo.findById(id).orElseThrow();
        a.setTitle(r.getTitle());
        a.setAppointmentDate(r.getAppointmentDate());
        a.setTime(r.getTime());
        a.setLocation(r.getLocation());
        a.setDoctor(r.getDoctor());
        a.setReason(r.getReason());
        return map(repo.save(a));
    }

    public void delete(Long id){ repo.deleteById(id); }

    public UiAppointmentReminderResponse map(UiAppointmentReminder a){
        return new UiAppointmentReminderResponse(
                a.getId(), a.getTitle(), a.getAppointmentDate(),
                a.getTime(), a.getLocation(), a.getDoctor(), a.getReason()
        );
    }
}

