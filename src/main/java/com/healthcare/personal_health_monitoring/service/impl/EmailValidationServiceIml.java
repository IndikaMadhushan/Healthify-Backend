package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.service.EmailValidationService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.regex.Pattern;

@Service
public class EmailValidationServiceIml implements EmailValidationService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");



    @Override
    public boolean isValidEmail(String email) {

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }

        try {
            String domain = email.substring(email.indexOf("@") + 1);
            InetAddress domainAdress = InetAddress.getByName(domain);
            return domainAdress != null;
        } catch (Exception e) {
            return false;
        }

    }
}
