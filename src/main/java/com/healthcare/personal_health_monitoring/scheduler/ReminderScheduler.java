package com.healthcare.personal_health_monitoring.scheduler;

import com.healthcare.personal_health_monitoring.entity.AppointmentReminder;
import com.healthcare.personal_health_monitoring.entity.MedicineReminder;
import com.healthcare.personal_health_monitoring.entity.OtherReminder;
import com.healthcare.personal_health_monitoring.entity.PeriodReminder;
import com.healthcare.personal_health_monitoring.repository.AppointmentReminderRepository;
import com.healthcare.personal_health_monitoring.repository.MedicineReminderRepository;
import com.healthcare.personal_health_monitoring.repository.OtherReminderRepository;
import com.healthcare.personal_health_monitoring.repository.PeriodReminderRepository;
import com.healthcare.personal_health_monitoring.service.EmailService;
import jakarta.validation.constraints.Email;
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

    private final MedicineReminderRepository medicineReminderRepository;
    private final AppointmentReminderRepository appointmentReminderRepository;
    private final OtherReminderRepository otherReminderRepository;
    private final PeriodReminderRepository periodReminderRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000) // every 60 second
    public void checkAndReminders(){
        List<MedicineReminder> reminders = medicineReminderRepository.findByActiveTrue();

        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();


        //       Medicine reminder
        for (MedicineReminder reminder : reminders) {
            if(reminder.getReminderType().name().equals("DAILY")){
                if(reminder.getTime().equals(now.withSecond(0).withNano(0))){
                    String email = reminder.getPatient().getUser().getEmail();
                    emailService.sendReminderEmail(
                            email,
                            reminder.getMedicineName(),
                            reminder.getTime().toString()
                    );

                }

                if(reminder.getReminderType().name().equals("SPECIFIC_DATE")){
                    if(reminder.getSpecificDate() != null  &&
                          reminder.getSpecificDate().equals(today) &&
                            reminder.getTime().equals(now.withSecond(0).withNano(0))){

                        String email = reminder.getPatient().getUser().getEmail();
                        emailService.sendReminderEmail(email, reminder.getMedicineName(), reminder.getTime().toString());
                    }
                }
            }
        }

        // for appointment reminder

        List<AppointmentReminder> appointments = appointmentReminderRepository.findAll();

        for (AppointmentReminder appt : appointments) {
            if (!appt.isCompleted()
                    && appt.getAppointmentDate().isEqual(today)
                    && appt.getAppointmentTime().equals(now)){

                String email = appt.getPatient().getUser().getEmail();

                emailService.sendAppointmentReminderEmail (
                        email,
                        appt.getDoctorName(),
                        appt.getHospital(),
                        appt.getAppointmentTime().toString()
                );
            }
        }

        // other reminders

        List<OtherReminder> others = otherReminderRepository.findByActiveTrue();

        for(OtherReminder reminder : others) {
            if(reminder.getReminderType().equals("DAILY")
                && reminder.getTime().equals(now)
            ){
                String email = reminder.getPatient().getUser().getEmail();
                emailService.sendOtherReminderEmail(
                        email,
                        reminder.getNote(),
                        reminder.getTime().toString()
                );
            }

            if(reminder.getReminderType().equals("SPECIFIC_DATE")
                    && reminder.getSpecificDate() != null
                    && reminder.getSpecificDate().equals(today)
                    && reminder.getTime().equals(now)
            ){
                String email = reminder.getPatient().getUser().getEmail();
                emailService.sendOtherReminderEmail(
                        email,
                        reminder.getNote(),
                        reminder.getTime().toString()
                );
            }
        }

        // period reminder for females

        List<PeriodReminder> periods = periodReminderRepository.findByActiveTrue();

        for(PeriodReminder reminder : periods){
            LocalDate nextDate = reminder.getNextPeriodDate();

            Long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), nextDate);

            if(daysLeft == 2 || daysLeft == 1 || daysLeft == 0 ){
                String email = reminder.getPatient().getUser().getEmail();

                emailService.sendPeriodReminderEmail(
                        email,
                        "Period Reminder",
                        "Your next period is on " + nextDate
                );
            }
        }

    }

}
