package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.PeriodReminderRequest;
import com.healthcare.personal_health_monitoring.dto.PeriodReminderResponse;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.PeriodTracker;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.PeriodTrackerRepository;
import com.healthcare.personal_health_monitoring.service.UiPeriodTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UiPeriodTrackerServiceImpl implements UiPeriodTrackerService {

    private final PeriodTrackerRepository trackerRepository;
    private final PatientRepository patientRepository;

    private Patient patient(User user) {
        return patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Override
    public PeriodReminderResponse saveOrUpdate(
            User user,
            PeriodReminderRequest request) {

        Patient patient = patient(user);

        // Deactivate existing tracker (only one active allowed)
        trackerRepository.findByPatientIdAndActiveTrue(patient.getId())
                .ifPresent(existing -> {
                    existing.setActive(false);
                    trackerRepository.save(existing);
                });

        PeriodTracker tracker = new PeriodTracker();
        tracker.setPatient(patient);
        tracker.setLastPeriodDate(request.getLastPeriodDate());
        tracker.setCycleLength(
                request.getCycleLength() != null ? request.getCycleLength() : 28
        );
        tracker.setPeriodDuration(request.getPeriodDuration());
        tracker.setNotes(request.getNotes());
        tracker.setActive(true);

        PeriodTracker saved = trackerRepository.save(tracker);

        return new PeriodReminderResponse(
                saved.getId(),
                saved.getLastPeriodDate(),
                saved.getCycleLength(),
                calculateNextPeriod(saved),
                saved.isActive()
        );
    }

    @Override
    public PeriodReminderResponse getActive(User user) {

        Patient patient = patient(user);

        return trackerRepository.findByPatientIdAndActiveTrue(patient.getId())
                .map(t -> new PeriodReminderResponse(
                        t.getId(),
                        t.getLastPeriodDate(),
                        t.getCycleLength(),
                        calculateNextPeriod(t),
                        t.isActive()
                ))
                .orElse(null);
    }

    @Override
    public void deactivate(Long trackerId) {
        PeriodTracker tracker = trackerRepository.findById(trackerId)
                .orElseThrow(() -> new RuntimeException("Tracker not found"));

        tracker.setActive(false);
        trackerRepository.save(tracker);
    }

    /* ===== Helper ===== */
    private LocalDate calculateNextPeriod(PeriodTracker tracker) {
        if (tracker.getLastPeriodDate() == null || tracker.getCycleLength() == null)
            return null;

        return tracker.getLastPeriodDate().plusDays(tracker.getCycleLength());
    }
}


