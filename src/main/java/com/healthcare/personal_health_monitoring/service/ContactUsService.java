package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.ContactUsRequest;

import java.util.Map;

public interface ContactUsService {
    Map<String, String> validate(ContactUsRequest request);

    void submit(ContactUsRequest request, String ipAddress, String userAgent);
}
