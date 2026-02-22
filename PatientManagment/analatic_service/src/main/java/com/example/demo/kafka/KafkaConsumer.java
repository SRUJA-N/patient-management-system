package com.example.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @KafkaListener(topics = "patient-events", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            String jsonString = new String(event);
            JsonNode patientEvent = objectMapper.readTree(jsonString);
            
            String patientId = patientEvent.has("patientId") ? patientEvent.get("patientId").asText() : "unknown";
            String name = patientEvent.has("name") ? patientEvent.get("name").asText() : "unknown";
            String email = patientEvent.has("email") ? patientEvent.get("email").asText() : "unknown";
            String eventType = patientEvent.has("eventType") ? patientEvent.get("eventType").asText() : "unknown";
            
            log.info("Received patient event: id={}, name={}, email={}, eventType={}",
                    patientId, name, email, eventType);
            
            // Process the event - in a real app, you'd store this in a database
            processPatientEvent(patientId, name, email, eventType);
            
        } catch (Exception e) {
            log.error("Error processing event: {}", e.getMessage());
        }
    }
    
    private void processPatientEvent(String patientId, String name, String email, String eventType) {
        // Add your analytics processing logic here
        log.info("Processing patient event for analytics: {} - {} ({})", eventType, name, patientId);
    }
}
