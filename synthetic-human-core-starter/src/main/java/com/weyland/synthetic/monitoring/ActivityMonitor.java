package com.weyland.synthetic.monitoring;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActivityMonitor {
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private final Counter taskCounter;
    private final Gauge activeTasksGauge;

    public ActivityMonitor(MeterRegistry meterRegistry) {
        // Регистрируем метрики явно
        this.taskCounter = Counter.builder("weyland.tasks.executed")
                .description("Total executed tasks")
                .register(meterRegistry);

        this.activeTasksGauge = Gauge.builder("synthetic.human.active.tasks", activeTasks, AtomicInteger::get)
                .description("Current active tasks")
                .strongReference(true) // Важно для Gauges!
                .register(meterRegistry);
    }

    public void incrementActiveTasks() {
        activeTasks.incrementAndGet();
        // Явное обновление метрики
        activeTasksGauge.measure();
    }

    public void decrementActiveTasks() {
        activeTasks.decrementAndGet();
        activeTasksGauge.measure();
    }

    public void incrementTaskCounter() {
        taskCounter.increment();
    }

    public int getActiveTasksCount() {
        return activeTasks.get();
    }
}