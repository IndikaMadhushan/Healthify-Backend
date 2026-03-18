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

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification OTP");
        message.setText("Your OTP for email verification is  " + otp);

        mailSender.send(message);
    }


    @Override
    public void sendRejectionEmail(String email, String doctorName) {
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
        mailSender.send(message);


    }

    @Override
    public void sendAccountActivatedEmail(String email, String userName) {
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
        mailSender.send(message);

    }

    @Override
    public void sendAccountDeactivatedEmail(String email, String userName) {
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
        mailSender.send(message);

    }
}
