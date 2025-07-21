package com.weyland.synthetic;

import com.weyland.synthetic.audit.AuditProducer;
import com.weyland.synthetic.config.SyntheticHumanProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner configCheck(SyntheticHumanProperties props) {
        return args -> {
            System.out.println("Аудит режим: " + props.getAudit().getMode());
        };
    }
}