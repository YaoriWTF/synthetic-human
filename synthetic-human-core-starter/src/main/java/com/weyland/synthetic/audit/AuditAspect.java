package com.weyland.synthetic.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weyland.synthetic.WeylandWatchingYou;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(weylandWatchingYou)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, WeylandWatchingYou weylandWatchingYou) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        // Логирование начала выполнения
        AuditEvent startEvent = AuditEvent.builder()
                .eventType("METHOD_START")
                .methodName(methodName)
                .author(weylandWatchingYou.author())
                .timestamp(Instant.now())
                .parameters(weylandWatchingYou.logParameters() ? serializeParameters(args) : null)
                .build();

        auditService.logEvent(startEvent);

        try {
            Object result = joinPoint.proceed();

            // Логирование успешного завершения
            AuditEvent successEvent = AuditEvent.builder()
                    .eventType("METHOD_SUCCESS")
                    .methodName(methodName)
                    .author(weylandWatchingYou.author())
                    .timestamp(Instant.now())
                    .result(weylandWatchingYou.logResult() ? serializeResult(result) : null)
                    .build();

            auditService.logEvent(successEvent);
            return result;
        } catch (Exception e) {
            // Логирование ошибки
            AuditEvent errorEvent = AuditEvent.builder()
                    .eventType("METHOD_FAILED")
                    .methodName(methodName)
                    .author(weylandWatchingYou.author())
                    .timestamp(Instant.now())
                    .errorMessage(e.getMessage())
                    .build();

            auditService.logEvent(errorEvent);
            throw e;
        }
    }

    private String serializeParameters(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            return "Failed to serialize parameters";
        }
    }

    private String serializeResult(Object result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return "Failed to serialize result";
        }
    }
}