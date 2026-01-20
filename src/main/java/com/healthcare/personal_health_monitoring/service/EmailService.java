package com.healthcare.personal_health_monitoring.service;

public interface EmailService {
    void sendReminderEmail(String toEmail, String medicineName, String time);

    void sendAppointmentReminderEmail(String email, String doctor, String hospital, String time);

   void sendOtherReminderEmail(
            String email,
            String note,
            String time
    );

   void sendPeriodReminderEmail(String email, String subject, String nextDate);

   void sendOtpEmail(String toEmail, String otp);

}
