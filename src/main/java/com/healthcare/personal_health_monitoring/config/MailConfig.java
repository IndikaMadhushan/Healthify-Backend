package com.healthcare.personal_health_monitoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

import java.util.Properties;

@Configuration
@Conditional(MailHostConfiguredCondition.class)
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(
            @Value("${MAIL_HOST}") String host,
            @Value("${MAIL_PORT:587}") int port,
            @Value("${MAIL_USERNAME:}") String username,
            @Value("${MAIL_PASSWORD:}") String password,
            @Value("${MAIL_SMTP_AUTH:false}") boolean smtpAuth,
            @Value("${MAIL_SMTP_STARTTLS_ENABLE:false}") boolean startTlsEnable,
            @Value("${MAIL_SMTP_STARTTLS_REQUIRED:false}") boolean startTlsRequired
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        if (StringUtils.hasText(username)) {
            mailSender.setUsername(username);
        }

        if (StringUtils.hasText(password)) {
            mailSender.setPassword(password);
        }

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", startTlsEnable);
        properties.put("mail.smtp.starttls.required", startTlsRequired);

        return mailSender;
    }
}
