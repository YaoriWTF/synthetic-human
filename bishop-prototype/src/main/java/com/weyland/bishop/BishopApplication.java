package com.weyland.bishop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = {
        "com.weyland.bishop",
        "com.weyland.synthetic"
})
public class BishopApplication {
    public static void main(String[] args) {
        SpringApplication.run(BishopApplication.class, args);
    }
}