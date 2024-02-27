package com.example.esperar_app.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("environments")
public record ConfigProperties(Resend resend, CloudPlatform cloudPlatform, Security security) {
    public record Resend(String apiKey) { }

    public record CloudPlatform(String provider, String apiKey, String secretKey) { }

    public record Security(String jwtSecretKey) { }

}
