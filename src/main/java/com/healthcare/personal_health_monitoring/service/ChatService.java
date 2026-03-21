package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.ChatRequest;
import com.healthcare.personal_health_monitoring.dto.ChatResponse;

public interface ChatService {
    ChatResponse chat(ChatRequest request);

    ChatResponse generateHealthTip();
}
