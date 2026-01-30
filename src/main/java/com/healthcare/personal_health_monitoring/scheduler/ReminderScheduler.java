package com.healthcare.personal_health_monitoring.scheduler;

import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.*;
import com.healthcare.personal_health_monitoring.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final UiMedicineReminderRepository medicineRepo;
    private final UiAppointmentReminderRepository appointmentRepo;
    private final UiOtherReminderRepository otherRepo;
    private final PeriodTrackerRepository periodRepo;
    private final EmailService emailService;

    // Every minute
    @Scheduled(fixedRate = 60000)
    public void sendReminders() {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        /* =====================
           MEDICINE REMINDERS
        ===================== */
        for (UiMedicineReminder r : medicineRepo.findAll()) {

            if (r.getTime() == null) continue;

            if (r.getTime().equals(now)) {
                emailService.sendReminderEmail(
                        r.getPatient().getUser().getEmail(),
                        r.getMedicineName(),
                        r.getTime().toString()
                );
            }
        }

        /* =====================
           APPOINTMENT REMINDERS
        ===================== */
        for (UiAppointmentReminder appt : appointmentRepo.findByCompletedFalse()) {

            if (appt.getAppointmentDate().isEqual(today)
                    && appt.getTime().equals(now)) {

                emailService.sendAppointmentReminderEmail(
                        appt.getPatient().getUser().getEmail(),
                        appt.getDoctor(),
                        appt.getLocation(),
                        appt.getTime().toString()
                );
            }
        }

        /* =====================
           OTHER REMINDERS
        ===================== */
        for (UiOtherReminder r : otherRepo.findAll()) {

            if (!r.getReminderDate().isEqual(today)) continue;

            LocalTime reminderTime =
                    r.getTime() != null ? r.getTime() : LocalTime.of(8, 0);

            if (reminderTime.equals(now)) {
                emailService.sendOtherReminderEmail(
                        r.getPatient().getUser().getEmail(),
                        r.getTitle(),
                        reminderTime.toString()
                );
            }
        }

        /* =====================
           PERIOD REMINDERS
        ===================== */
        for (PeriodTracker tracker : periodRepo.findByActiveTrue()) {

            LocalDate nextPeriod =
                    tracker.getLastPeriodDate()
                            .plusDays(tracker.getCycleLength());

            long daysLeft = ChronoUnit.DAYS.between(today, nextPeriod);

            if (daysLeft == 2 || daysLeft == 1) {
                emailService.sendPeriodReminderEmail(
                        tracker.getPatient().getUser().getEmail(),
                        "Upcoming Period Reminder",
                        "Your next period is expected on " + nextPeriod
                );
            }
        }
    }
}

