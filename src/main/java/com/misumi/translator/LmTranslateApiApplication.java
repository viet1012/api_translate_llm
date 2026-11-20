package com.misumi.translator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LmTranslateApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(LmTranslateApiApplication.class, args);
    }
}
