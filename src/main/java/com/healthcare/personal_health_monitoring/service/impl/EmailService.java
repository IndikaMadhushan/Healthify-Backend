package com.healthcare.personal_health_monitoring.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendApprovalMail(String toEmail, int clinicPageId) {

        String approveLink =
                "http://localhost:8080/api/v1/cpage/approve-edit/"
                        + clinicPageId;


        String htmlContent =
                "<h3>Doctor requests permission to edit your medical record</h3>" +
                        "<p>Please click approve:</p>" +
                        "<a href='" + approveLink + "' " +
                        "style='padding:10px 20px;background-color:green;color:white;" +
                        "text-decoration:none;'>APPROVE</a>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Approval required to edit medical record");
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }
}