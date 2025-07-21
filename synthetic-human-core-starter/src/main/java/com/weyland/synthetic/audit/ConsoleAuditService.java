package com.weyland.synthetic.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
public class ConsoleAuditService implements AuditService {
    private static final Logger log = LoggerFactory.getLogger(ConsoleAuditService.class);

    @Override
    public void logEvent(AuditEvent event) {
        log.info("[AUDIT] Event: {}, Method: {}, Author: {}, Time: {}",
                event.getEventType(),
                event.getMethodName(),
                event.getAuthor(),
                event.getTimestamp());

        if (event.getParameters() != null) {
            log.info("Parameters: {}", event.getParameters());
        }
        if (event.getResult() != null) {
            log.info("Result: {}", event.getResult());
        }
        if (event.getErrorMessage() != null) {
            log.error("Error: {}", event.getErrorMessage());
        }
    }
}