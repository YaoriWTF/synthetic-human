package com.weyland.synthetic.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class AuditProducer {
    private static final String AUDIT_TOPIC = "audit-events";
    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    public void logEvent(AuditEvent event) {
        kafkaTemplate.send(AUDIT_TOPIC, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("Audit event sent successfully: " + event);
                    } else {
                        System.err.println("Failed to send audit event: " + ex.getMessage());
                    }
                });
    }
}