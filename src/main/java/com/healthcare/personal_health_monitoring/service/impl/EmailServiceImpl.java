package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendReminderEmail(
            String toEmail,
            String medicineName,
            String time
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Medicine Reminder");
        message.setText("It's time to take Your Medicine: " + medicineName + "at" + time);

        mailSender.send(message);
    }

    @Override
    public void sendAppointmentReminderEmail(
            String email,
            String doctor,
            String hospital,
            String time) {
        String subject = "Appointment Reminder";
        String body = "You have an Appointment today at " + time + " with Dr. "
                + doctor + " at " + hospital;


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }

    @Override
    public void sendOtherReminderEmail(
            String email,
            String note,
            String time
            ){
        String subject = "Other Appointment Reminder";
        String body = "You have an appointment today at " + time + " for : " + note;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendPeriodReminderEmail(String email, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


}
