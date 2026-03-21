package com.healthcare.personal_health_monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.personal_health_monitoring.dto.ChatRequest;
import com.healthcare.personal_health_monitoring.dto.ChatResponse;
import com.healthcare.personal_health_monitoring.service.ChatService;
import com.healthcare.personal_health_monitoring.service.PatientContextService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/chat")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private PatientContextService patientContextService;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        log.info("Received chat message: {}", request.getMessage());

        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ChatResponse("Message cannot be empty", null, false));
        }

        ChatResponse response = chatService.chat(request);

        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/health-tip")
    public ResponseEntity<ChatResponse> getHealthTip() {
        log.info("Generating health tip");
        ChatResponse response = chatService.generateHealthTip();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chatbot API is running");
    }

    @GetMapping("/patient-context/{patientId}")
    public ResponseEntity<?> getPatientContext(@PathVariable Long patientId) {
        log.info("Retrieving patient context for patient: {}", patientId);
        try {
            String context = patientContextService.getPatientHealthContext(patientId);
            if (context.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Patient not found with ID: " + patientId);
            }
            return ResponseEntity.ok(context);
        } catch (Exception e) {
            log.error("Error retrieving patient context", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
