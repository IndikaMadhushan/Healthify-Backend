package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    private final Optional<JavaMailSender> mailSender;

    private JavaMailSender getMailSender(String action, String recipient) {
        if (mailSender.isEmpty()) {
            log.warn(
                    "Skipping email action '{}' for '{}' because mail is not configured",
                    action,
                    recipient
            );
            return null;
        }

        return mailSender.orElseThrow();
    }

    @Override
    public void sendReminderEmail(
            String toEmail,
            String medicineName,
            String time
    ) {
        JavaMailSender configuredMailSender =
                getMailSender("sendReminderEmail", toEmail);

        if (configuredMailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Medicine Reminder");
        message.setText("It's time to take Your Medicine: " + medicineName + "at" + time);

        configuredMailSender.send(message);
    }

    @Override
    public void sendAppointmentReminderEmail(
            String email,
            String doctor,
            String hospital,
            String time) {
        JavaMailSender configuredMailSender =
                getMailSender("sendAppointmentReminderEmail", email);

        if (configuredMailSender == null) {
            return;
        }

        String subject = "Appointment Reminder";
        String body = "You have an Appointment today at " + time + " with Dr. "
                + doctor + " at " + hospital;


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        configuredMailSender.send(message);

    }

    @Override
    public void sendOtherReminderEmail(
            String email,
            String note,
            String time
            ){
        JavaMailSender configuredMailSender =
                getMailSender("sendOtherReminderEmail", email);

        if (configuredMailSender == null) {
            return;
        }

        String subject = "Other Appointment Reminder";
        String body = "You have an appointment today at " + time + " for : " + note;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        configuredMailSender.send(message);
    }

    @Override
    public void sendPeriodReminderEmail(String email, String subject, String body) {
        JavaMailSender configuredMailSender =
                getMailSender("sendPeriodReminderEmail", email);

        if (configuredMailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        configuredMailSender.send(message);
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        JavaMailSender configuredMailSender =
                getMailSender("sendOtpEmail", toEmail);

        if (configuredMailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification OTP");
        message.setText("Your OTP for email verification is  " + otp);

        configuredMailSender.send(message);
    }


    @Override
    public void sendRejectionEmail(String email, String doctorName) {
        JavaMailSender configuredMailSender =
                getMailSender("sendRejectionEmail", email);

        if (configuredMailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        String subject = "Doctor Registration - Application Rejected";
        message.setSubject(subject);
        String body = String.format(
                "Dear %s,\n\n" +
                        "We regret to inform you that your doctor registration application has been rejected.\n\n" +
                        "If you believe this is an error or would like to reapply, please contact our support team.\n\n" +
                        "Best regards,\n" +
                        "Healthify Admin Team",
                doctorName
        );
        message.setText(body);
        configuredMailSender.send(message);


    }

    @Override
    public void sendAccountActivatedEmail(String email, String userName) {
        JavaMailSender configuredMailSender =
                getMailSender("sendAccountActivatedEmail", email);

        if (configuredMailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        String subject = "Account Activated - Healthify";
        message.setSubject(subject);
        String body = String.format(
                "Dear %s,\n\n" +
                        "Your Healthify account has been activated by the administrator.\n\n" +
                        "You can now log in and access all features.\n\n" +
                        "Best regards,\n" +
                        "Healthify Admin Team",
                userName
        );
        message.setText(body);
        configuredMailSender.send(message);

    }

    @Override
    public void sendAccountDeactivatedEmail(String email, String userName) {
        JavaMailSender configuredMailSender =
                getMailSender("sendAccountDeactivatedEmail", email);

        if (configuredMailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        String subject = "Account Status Update - Healthify";
        message.setSubject(subject);
        String body = String.format(
                "Dear %s,\n\n" +
                        "Your Healthify account has been temporarily deactivated by the administrator.\n\n" +
                        "If you believe this is an error, please contact our support team.\n\n" +
                        "Best regards,\n" +
                        "Healthify Admin Team",
                userName
        );
        message.setText(body);
        configuredMailSender.send(message);

    }
}
