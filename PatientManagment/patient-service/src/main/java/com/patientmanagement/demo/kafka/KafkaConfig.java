package com.patientmanagement.demo.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String PATIENT_EVENTS_TOPIC = "patient-events";

    @Bean
    public NewTopic patientEventsTopic() {
        return TopicBuilder.name(PATIENT_EVENTS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
