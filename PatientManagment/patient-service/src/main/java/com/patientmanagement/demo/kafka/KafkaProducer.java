package com.patientmanagement.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String key, byte[] message) {
        kafkaTemplate.send(topic, key, message)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to send message to topic {}: {}", topic, ex.getMessage());
                } else {
                    logger.info("Message sent successfully to topic {} partition {} offset {}", 
                        topic, 
                        result.getRecordMetadata().partition(), 
                        result.getRecordMetadata().offset());
                }
            });
    }
}
