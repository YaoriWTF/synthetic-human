package com.weyland.synthetic.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weyland")
@Getter
@Setter
public class SyntheticHumanProperties {
    private Audit audit = new Audit();

    @Getter
    @Setter
    public static class Audit {
        private String mode = "console";
    }
}