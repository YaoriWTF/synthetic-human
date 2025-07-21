package com.weyland.synthetic.audit;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class AuditEvent {
    private final String eventType;
    private final String methodName;
    private final String author;
    private final Instant timestamp;
    private final String parameters;
    private final String result;
    private final String errorMessage;

    // Основной конструктор
    public AuditEvent(String eventType, String methodName, String author,
                      Instant timestamp, String parameters,
                      String result, String errorMessage) {
        this.eventType = eventType;
        this.methodName = methodName;
        this.author = author;
        this.timestamp = timestamp;
        this.parameters = parameters;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    // Упрощенный конструктор
    public AuditEvent(String eventType, String methodName, String author, Instant timestamp) {
        this(eventType, methodName, author, timestamp, null, null, null);
    }

    // Статический фабричный метод
    public static AuditEvent createBasic(String eventType, String methodName, String author) {
        return new AuditEvent(eventType, methodName, author, Instant.now());
    }
}