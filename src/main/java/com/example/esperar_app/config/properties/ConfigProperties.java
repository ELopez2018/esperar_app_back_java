package com.example.esperar_app.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("environments")
public record ConfigProperties(Resend resend) {
    public record Resend(String apiKey) { }


}
