package com.healthcare.personal_health_monitoring.config;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(
            @Value("${app.mail.host:}") String host,
            @Value("${app.mail.port:587}") int port,
            @Value("${app.mail.username:}") String username,
            @Value("${app.mail.password:}") String password,
            @Value("${app.mail.smtp.auth:true}") boolean smtpAuth,
            @Value("${app.mail.smtp.starttls.enable:true}") boolean startTlsEnable,
            @Value("${app.mail.smtp.starttls.required:true}") boolean startTlsRequired
    ) {
        if (host.isBlank() || username.isBlank() || password.isBlank()) {
            return new NoOpJavaMailSender();
        }

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);

        Properties properties = sender.getJavaMailProperties();
        properties.put("mail.smtp.auth", String.valueOf(smtpAuth));
        properties.put("mail.smtp.starttls.enable", String.valueOf(startTlsEnable));
        properties.put("mail.smtp.starttls.required", String.valueOf(startTlsRequired));

        return sender;
    }

    private static final class NoOpJavaMailSender extends JavaMailSenderImpl {
        private static final Logger log = LoggerFactory.getLogger(NoOpJavaMailSender.class);

        @Override
        public void send(SimpleMailMessage simpleMessage) {
            log.warn("Mail is disabled because SMTP settings are not configured. Skipping simple mail send.");
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) {
            log.warn("Mail is disabled because SMTP settings are not configured. Skipping {} simple mail message(s).", simpleMessages.length);
        }

        @Override
        public void send(MimeMessage mimeMessage) {
            log.warn("Mail is disabled because SMTP settings are not configured. Skipping MIME mail send.");
        }

        @Override
        public void send(MimeMessage... mimeMessages) {
            log.warn("Mail is disabled because SMTP settings are not configured. Skipping {} MIME mail message(s).", mimeMessages.length);
        }

        @Override
        public void send(MimeMessagePreparator mimeMessagePreparator) {
            log.warn("Mail is disabled because SMTP settings are not configured. Skipping prepared MIME mail send.");
        }

        @Override
        public void send(MimeMessagePreparator... mimeMessagePreparators) {
            log.warn("Mail is disabled because SMTP settings are not configured. Skipping {} prepared MIME mail message(s).", mimeMessagePreparators.length);
        }
    }
}
