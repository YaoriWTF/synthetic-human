package com.weyland.synthetic.command;

import com.weyland.synthetic.audit.AuditEvent;
import com.weyland.synthetic.audit.AuditProducer;
import com.weyland.synthetic.exception.SyntheticHumanException;
import com.weyland.synthetic.monitoring.ActivityMonitor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.*;

@Component
public class CommandQueue {
    private static final int MAX_QUEUE_SIZE = 100;
    private final ThreadPoolExecutor executor;
    private final ActivityMonitor activityMonitor;
    private final AuditProducer auditProducer;

    public CommandQueue(ActivityMonitor activityMonitor, AuditProducer auditProducer) {
        this.activityMonitor = activityMonitor;
        this.auditProducer = auditProducer;
        this.executor = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(MAX_QUEUE_SIZE), new ThreadPoolExecutor.AbortPolicy());
    }

    public <T> Future<CommandResult<T>> submit(Command<T> command) throws SyntheticHumanException {
        try {
            activityMonitor.incrementActiveTasks();

            auditProducer.logEvent(new AuditEvent("COMMAND_QUEUED", command.getMetadata().description(), command.getMetadata().author(), Instant.now()));

            return executor.submit(() -> {
                try {
                    CommandResult<T> result = command.execute();
                    return result;
                } finally {
                    activityMonitor.decrementActiveTasks();
                }
            });
        } catch (RejectedExecutionException e) {
            activityMonitor.decrementActiveTasks();
            auditProducer.logEvent(new AuditEvent("QUEUE_OVERFLOW", "Command queue is full", command.getMetadata().author(), Instant.now()));
            throw new SyntheticHumanException("Command queue is full. Please try again later.");
        }
    }

    public int getQueueSize() {
        return executor.getQueue().size();
    }

    public int getActiveTaskCount() {
        return executor.getActiveCount();
    }
}