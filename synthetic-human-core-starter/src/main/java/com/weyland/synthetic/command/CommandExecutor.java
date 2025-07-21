package com.weyland.synthetic.command;

import com.weyland.synthetic.audit.AuditEvent;
import com.weyland.synthetic.audit.AuditProducer;
import com.weyland.synthetic.exception.SyntheticHumanException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class CommandExecutor {
    private final CommandQueue commandQueue;
    private final AuditProducer auditProducer;
    private final CommandValidator validator;
    private final CriticalCommandExecutor criticalCommandExecutor;

    public <T> Future<CommandResult<T>> execute(Command<T> command) {
        try {
            // Валидация команды
            validator.validate(command.getMetadata());

            // Аудит получения команды
            auditProducer.logEvent(new AuditEvent("COMMAND_RECEIVED", command.getMetadata().description(), command.getMetadata().author(), command.getMetadata().time()));

            // Обработка в зависимости от приоритета
            if (command.getMetadata().priority() == CommandMetadata.Priority.CRITICAL) {
                return criticalCommandExecutor.executeCritical(command);
            } else {
                return commandQueue.submit(command);
            }
        } catch (SyntheticHumanException e) {
            auditProducer.logEvent(new AuditEvent("COMMAND_REJECTED", "Validation failed: " + e.getMessage(), command.getMetadata().author(), Instant.now()));
            throw e;
        }
    }
}