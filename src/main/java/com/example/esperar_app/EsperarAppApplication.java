package com.example.esperar_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EsperarAppApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EsperarAppApplication.class, args);
    }

}
