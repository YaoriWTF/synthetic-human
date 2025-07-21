package com.weyland.bishop;

import com.weyland.synthetic.audit.AuditProducer;
import com.weyland.synthetic.command.CommandExecutor;
import com.weyland.synthetic.command.CommandQueue;
import com.weyland.synthetic.command.CommandValidator;
import com.weyland.synthetic.command.CriticalCommandExecutor;
import com.weyland.synthetic.config.MetricsConfig;
import com.weyland.synthetic.config.SyntheticHumanAutoConfiguration;
import com.weyland.synthetic.config.SyntheticHumanProperties;
import com.weyland.synthetic.monitoring.ActivityMonitor;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(SyntheticHumanProperties.class)
@Import({SyntheticHumanAutoConfiguration.class, MetricsConfig.class})
public class BishopConfig {

    // Бин для кастомизации метрик
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCustomizer() {
        return registry -> registry.config().commonTags("application", "bishop-prototype");
    }

    // Бин ActivityMonitor с интеграцией метрик
    @Bean
    public ActivityMonitor activityMonitor(MeterRegistry meterRegistry) {
        return new ActivityMonitor(meterRegistry);
    }

    // Бин CommandExecutor с полной настройкой
    @Bean
    public CommandExecutor commandExecutor(CommandQueue commandQueue, AuditProducer auditProducer, CommandValidator validator, CriticalCommandExecutor criticalCommandExecutor) {
        return new CommandExecutor(commandQueue, auditProducer, validator, criticalCommandExecutor);
    }

    // Бин CommandValidator
    @Bean
    public CommandValidator commandValidator() {
        return new CommandValidator();
    }

    // Бин для критических команд
    @Bean
    public CriticalCommandExecutor criticalCommandExecutor(AuditProducer auditProducer) {
        return new CriticalCommandExecutor(auditProducer);
    }

}