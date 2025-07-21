package com.weyland.synthetic.audit;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "weyland.audit.mode", havingValue = "kafka")
@RequiredArgsConstructor
public class KafkaAuditService implements AuditService {
    private static final Logger log = LoggerFactory.getLogger(KafkaAuditService.class);
    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    @Override
    public void logEvent(AuditEvent event) {
        kafkaTemplate.send("weyland-audit-events", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Audit event sent to Kafka: {}", event);
                    } else {
                        log.error("Failed to send audit event to Kafka", ex);
                    }
                });
    }
}
