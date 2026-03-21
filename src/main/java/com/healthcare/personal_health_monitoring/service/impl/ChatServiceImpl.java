package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.ChatRequest;
import com.healthcare.personal_health_monitoring.service.ChatService;
import com.healthcare.personal_health_monitoring.service.PatientContextService;
import com.healthcare.personal_health_monitoring.dto.ChatResponse;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Autowired
    private PatientContextService patientContextService;

    @Autowired
    private PatientRepository patientRepository;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            String conversationId = request.getConversationId() != null ? request.getConversationId()
                    : UUID.randomUUID().toString();

            log.info("Processing chat request for conversation: {}", conversationId);

            String systemPrompt = buildSystemPrompt(request.getUserId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            String requestBody = String.format(
                    "{\"model\": \"%s\", \"messages\": [{\"role\": \"system\", \"content\": \"%s\"}, " +
                            "{\"role\": \"user\", \"content\": \"%s\"}], \"temperature\": 0.7, \"max_tokens\": 500}",
                    MODEL,
                    escapeJson(systemPrompt),
                    escapeJson(request.getMessage()));

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            try {
                String response = restTemplate.postForObject(OPENAI_API_URL, entity, String.class);
                String aiMessage = parseOpenAiResponse(response);

                ChatResponse chatResponse = new ChatResponse(aiMessage, conversationId);
                log.info("Chat response sent successfully for conversation: {}", conversationId);
                return chatResponse;
            } catch (RestClientException e) {
                log.error("Error communicating with OpenAI API", e);
                return new ChatResponse(
                        "I apologize, but I'm currently unable to process your request. Please try again later.",
                        conversationId,
                        false);
            }
        } catch (Exception e) {
            log.error("Error processing chat request", e);
            return new ChatResponse("An error occurred while processing your request",
                    UUID.randomUUID().toString(), false);
        }
    }

    private String buildSystemPrompt(String userId) {
        String basePrompt = "You are a helpful health monitoring assistant. Provide accurate, " +
                "supportive health information and wellness advice. Always encourage users to consult " +
                "with healthcare professionals for medical concerns.";

        if (userId != null && !userId.isEmpty()) {
            try {
                Long patientId = Long.parseLong(userId);
                String patientContext = patientContextService.getPatientHealthContext(patientId);
                if (!patientContext.isEmpty()) {
                    basePrompt += "\n\n=== PATIENT CONTEXT ===\n" + patientContext +
                            "\n=== END CONTEXT ===\n\nUse this patient information to provide personalized health advice.";
                    log.info("Patient context added to system prompt for patient: {}", patientId);
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid userId format: {}", userId);
            }
        }

        return basePrompt;
    }

    @Override
    public ChatResponse generateHealthTip() {
        try {
            String conversationId = UUID.randomUUID().toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            String requestBody = String.format(
                    "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"Generate a " +
                            "practical health and wellness tip for daily living. Keep it concise and actionable.\"}], "
                            +
                            "\"temperature\": 0.8, \"max_tokens\": 150}",
                    MODEL);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            try {
                String response = restTemplate.postForObject(OPENAI_API_URL, entity, String.class);
                String healthTip = parseOpenAiResponse(response);

                return new ChatResponse(healthTip, conversationId);
            } catch (RestClientException e) {
                log.error("Error generating health tip from OpenAI", e);
                return new ChatResponse("Stay hydrated and exercise regularly!", conversationId);
            }
        } catch (Exception e) {
            log.error("Error generating health tip", e);
            return new ChatResponse("Stay healthy!", UUID.randomUUID().toString());
        }
    }

    private String parseOpenAiResponse(String response) throws Exception {
        JsonNode root = objectMapper.readTree(response);
        return root.path("choices").get(0).path("message").path("content").asText();
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
