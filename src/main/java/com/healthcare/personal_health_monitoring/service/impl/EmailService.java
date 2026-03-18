package com.healthcare.personal_health_monitoring.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailService.class);

    private final Optional<JavaMailSender> mailSender;

    public void sendApprovalMail(String toEmail, int clinicPageId) {
        if (mailSender.isEmpty()) {
            log.warn(
                    "Skipping email action 'sendApprovalMail' for '{}' because mail is not configured",
                    toEmail
            );
            return;
        }

        String approveLink =
                "https://healthify.dev/api/v1/cpage/approve-edit/"
                        + clinicPageId;


        String htmlContent =
                "<h3>Doctor requests permission to edit your medical record</h3>" +
                        "<p>Please click approve:</p>" +
                        "<a href='" + approveLink + "' " +
                        "style='padding:10px 20px;background-color:green;color:white;" +
                        "text-decoration:none;'>APPROVE</a>";

        try {
            MimeMessage message = mailSender.orElseThrow().createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Approval required to edit medical record");
            helper.setText(htmlContent, true); // true = HTML

            mailSender.orElseThrow().send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }
}
