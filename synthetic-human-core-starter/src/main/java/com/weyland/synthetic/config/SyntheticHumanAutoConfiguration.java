package com.weyland.synthetic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weyland.synthetic.audit.*;
import com.weyland.synthetic.command.CommandQueue;
import com.weyland.synthetic.monitoring.ActivityMonitor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(SyntheticHumanProperties.class)
public class SyntheticHumanAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditService auditService(SyntheticHumanProperties properties, ObjectProvider<KafkaTemplate<String, AuditEvent>> kafkaTemplate) {
        if ("kafka".equalsIgnoreCase(properties.getAudit().getMode())) {
            KafkaTemplate<String, AuditEvent> template = kafkaTemplate.getIfAvailable();
            if (template != null) {
                return new KafkaAuditService(template);
            }
            System.err.println("Kafka mode requested but template not available. Falling back to console");
        }
        return new ConsoleAuditService();
    }

    @Bean
    public AuditAspect auditAspect(AuditService auditService, ObjectMapper objectMapper) {
        return new AuditAspect(auditService, objectMapper);
    }

    @Bean
    public CommandQueue commandQueue(ActivityMonitor activityMonitor, AuditProducer auditProducer) {
        return new CommandQueue(activityMonitor, auditProducer);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}