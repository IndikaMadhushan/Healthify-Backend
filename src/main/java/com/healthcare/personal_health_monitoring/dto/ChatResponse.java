package com.healthcare.personal_health_monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String message;
    private String conversationId;
    private Long timestamp;
    private Boolean success;
    private String error;

    public ChatResponse(String message, String conversationId) {
        this.message = message;
        this.conversationId = conversationId;
        this.timestamp = System.currentTimeMillis();
        this.success = true;
    }

    public ChatResponse(String error, String conversationId, Boolean success) {
        this.error = error;
        this.conversationId = conversationId;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
    }
}
