package com.patientmanagement.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = KafkaConfig.PATIENT_EVENTS_TOPIC, groupId = "patient-service-group")
    public void consumePatientEvent(String key, byte[] message) {
        logger.info("Received Kafka message with key: {}", key);
        logger.info("Message payload: {}", new String(message));
        
        // Process the patient event here
        // You can deserialize the message and perform actions
    }
}
