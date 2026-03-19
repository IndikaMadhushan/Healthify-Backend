package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.ContactUsRequest;
import com.healthcare.personal_health_monitoring.exception.TooManyContactRequestsException;
import com.healthcare.personal_health_monitoring.service.ContactUsService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class ContactUsServiceImpl implements ContactUsService {

    private static final Logger log = LoggerFactory.getLogger(ContactUsServiceImpl.class);
    private static final int MAX_SUBMISSIONS_PER_HOUR = 5;
    private static final Duration RATE_LIMIT_WINDOW = Duration.ofHours(1);
    private static final DateTimeFormatter SUBMITTED_AT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Validator validator;
    private final JavaMailSender mailSender;

    private final ConcurrentMap<String, Deque<Instant>> submissionsByIp = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Deque<Instant>> submissionsByEmail = new ConcurrentHashMap<>();

    @Value("${contact-us.to-email:}")
    private String contactUsToEmail;

    @Value("${contact-us.from-email:}")
    private String contactUsFromEmail;

    @Override
    public Map<String, String> validate(ContactUsRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        validator.validate(request).stream()
                .sorted(Comparator.comparing(violation -> violation.getPropertyPath().toString()))
                .forEach(violation -> errors.putIfAbsent(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ));

        return errors;
    }

    @Override
    public void submit(ContactUsRequest request, String ipAddress, String userAgent) {
        enforceRateLimit(ipAddress, request.email());

        if (contactUsToEmail == null || contactUsToEmail.isBlank()) {
            log.error("Contact Us recipient email is not configured.");
            throw new IllegalStateException("Failed to send contact message");
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(stripHeaderBreaks(contactUsToEmail));
        if (contactUsFromEmail != null && !contactUsFromEmail.isBlank()) {
            mailMessage.setFrom(stripHeaderBreaks(contactUsFromEmail));
        }
        mailMessage.setReplyTo(stripHeaderBreaks(request.email()));
        mailMessage.setSubject(buildSubject(request.subject()));
        mailMessage.setText(buildBody(request, ipAddress, userAgent, LocalDateTime.now()));

        try {
            mailSender.send(mailMessage);
        } catch (MailException ex) {
            log.error("Failed to send Contact Us message for {}", request.email(), ex);
            throw new IllegalStateException("Failed to send contact message", ex);
        }
    }

    private void enforceRateLimit(String ipAddress, String email) {
        if (!allowRequest(submissionsByIp, normalizeRateLimitKey(ipAddress))
                || !allowRequest(submissionsByEmail, normalizeRateLimitKey(email))) {
            throw new TooManyContactRequestsException("Too many contact requests. Please try again later.");
        }
    }

    private boolean allowRequest(ConcurrentMap<String, Deque<Instant>> store, String key) {
        if (key == null) {
            return true;
        }

        Instant now = Instant.now();
        Deque<Instant> timestamps = store.computeIfAbsent(key, ignored -> new ArrayDeque<>());

        synchronized (timestamps) {
            purgeExpiredEntries(timestamps, now);
            if (timestamps.size() >= MAX_SUBMISSIONS_PER_HOUR) {
                return false;
            }
            timestamps.addLast(now);
            return true;
        }
    }

    private void purgeExpiredEntries(Deque<Instant> timestamps, Instant now) {
        Instant cutoff = now.minus(RATE_LIMIT_WINDOW);
        while (!timestamps.isEmpty() && timestamps.peekFirst().isBefore(cutoff)) {
            timestamps.removeFirst();
        }
    }

    private String buildSubject(String subject) {
        String safeSubject = subject == null ? "General Inquiry" : subject;
        return "[Healthify Contact Us] " + stripHeaderBreaks(safeSubject);
    }

    private String buildBody(
            ContactUsRequest request,
            String ipAddress,
            String userAgent,
            LocalDateTime submittedAt
    ) {
        return String.join("\n",
                "Name: " + request.name(),
                "Email: " + request.email(),
                "Phone: " + valueOrNotProvided(request.phone()),
                "Subject: " + valueOrNotProvided(request.subject()),
                "Submitted time: " + submittedAt.format(SUBMITTED_AT_FORMAT),
                "IP address: " + valueOrNotProvided(ipAddress),
                "User-Agent: " + valueOrNotProvided(userAgent),
                "",
                "Message:",
                request.message()
        );
    }

    private String valueOrNotProvided(String value) {
        return value == null || value.isBlank() ? "Not provided" : value;
    }

    private String normalizeRateLimitKey(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed.toLowerCase();
    }

    private String stripHeaderBreaks(String value) {
        return value.replace("\r", " ").replace("\n", " ").trim();
    }
}
