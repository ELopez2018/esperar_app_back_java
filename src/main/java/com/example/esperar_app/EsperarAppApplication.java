package com.example.esperar_app;

import com.example.esperar_app.config.properties.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ConfigProperties.class)
public class EsperarAppApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EsperarAppApplication.class, args);
    }

}
