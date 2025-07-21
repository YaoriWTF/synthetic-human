package com.weyland.synthetic.config;

import com.weyland.synthetic.command.CommandQueue;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "synthetic-human");
    }

    @Bean
    public Counter taskCounter(MeterRegistry registry) {
        return Counter.builder("weyland.tasks.executed")
                .description("Total number of executed tasks")
                .tag("author", "unknown")
                .register(registry);
    }

    @Bean
    public Gauge taskQueueGauge(CommandQueue queue, MeterRegistry registry) {
        return Gauge.builder("weyland.tasks.queue.size", queue::getQueueSize)
                .description("Current task queue size")
                .register(registry);
    }
}
