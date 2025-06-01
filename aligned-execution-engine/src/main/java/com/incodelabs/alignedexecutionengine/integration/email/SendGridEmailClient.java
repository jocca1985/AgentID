package com.incodelabs.alignedexecutionengine.integration.email;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incodelabs.alignedexecutionengine.integration.email.dto.EmailRequest;
import com.incodelabs.alignedexecutionengine.integration.email.dto.EmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendGridEmailClient implements EmailClientApi {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    
    @Value("${sendgrid.api.url:https://api.sendgrid.com/v3/mail/send}")
    private String sendGridApiUrl;
    
    @Override
    public EmailResponse sendEmail(EmailRequest emailRequest) {
        try {
            log.info("Sending email to: {} with subject: {}", emailRequest.getToEmail(), emailRequest.getSubject());
            
            // Build SendGrid API request body
            Map<String, Object> requestBody = buildSendGridRequest(emailRequest);
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + sendGridApiKey);
            headers.set("Content-Type", "application/json");
            
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
            
            // Make API call
            ResponseEntity<String> response = restTemplate.exchange(
                sendGridApiUrl, 
                HttpMethod.POST, 
                httpEntity, 
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully to: {}", emailRequest.getToEmail());
                return EmailResponse.builder()
                    .success(true)
                    .message("Email sent successfully")
                    .messageId(extractMessageId(response.getBody()))
                    .build();
            } else {
                log.error("Failed to send email. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
                return EmailResponse.builder()
                    .success(false)
                    .message("Failed to send email: " + response.getStatusCode())
                    .build();
            }
            
        } catch (Exception e) {
            log.error("Error sending email to: {}", emailRequest.getToEmail(), e);
            return EmailResponse.builder()
                .success(false)
                .message("Error sending email: " + e.getMessage())
                .build();
        }
    }
    
    private Map<String, Object> buildSendGridRequest(EmailRequest emailRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // Personalizations
        Map<String, Object> personalization = new HashMap<>();
        personalization.put("to", List.of(Map.of("email", emailRequest.getToEmail())));
        requestBody.put("personalizations", List.of(personalization));
        
        // From
        requestBody.put("from", Map.of("email", emailRequest.getFromEmail()));
        
        // Subject
        requestBody.put("subject", emailRequest.getSubject());
        
        // Content
        Map<String, Object> content = new HashMap<>();
        content.put("type", "text/plain");
        content.put("value", emailRequest.getContent());
        requestBody.put("content", List.of(content));
        
        return requestBody;
    }
    
    private String extractMessageId(String responseBody) {
        try {
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                JsonNode messageIdNode = jsonNode.get("message_id");
                return messageIdNode != null ? messageIdNode.asText() : null;
            }
        } catch (Exception e) {
            log.warn("Failed to extract message ID from response", e);
        }
        return null;
    }
}