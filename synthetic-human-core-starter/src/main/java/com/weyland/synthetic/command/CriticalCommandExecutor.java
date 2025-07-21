package com.weyland.synthetic.command;

import com.weyland.synthetic.audit.AuditEvent;
import com.weyland.synthetic.audit.AuditProducer;
import com.weyland.synthetic.exception.SyntheticHumanException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class CriticalCommandExecutor {
    private final AuditProducer auditProducer;

    public <T> Future<CommandResult<T>> executeCritical(Command<T> command) {
        return CompletableFuture.supplyAsync(() -> {
            auditProducer.logEvent(new AuditEvent(
                    "CRITICAL_COMMAND_STARTED",
                    command.getMetadata().description(),
                    command.getMetadata().author(),
                    Instant.now()
            ));

            try {
                CommandResult<T> result = command.execute();

                auditProducer.logEvent(new AuditEvent(
                        "COMMAND_COMPLETED",
                        "Critical command executed successfully",
                        command.getMetadata().author(),
                        Instant.now()
                ));

                return result;
            } catch (Exception e) {
                auditProducer.logEvent(new AuditEvent(
                        "COMMAND_FAILED",
                        "Critical command failed: " + e.getMessage(),
                        command.getMetadata().author(),
                        Instant.now()
                ));
                throw new SyntheticHumanException("Critical command execution failed", e);
            }
        });
    }
}
