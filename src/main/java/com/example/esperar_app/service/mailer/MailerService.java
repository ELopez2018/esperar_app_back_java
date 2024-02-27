package com.example.esperar_app.service.mailer;

public interface MailerService {
    String sendMail();

    void sendChangePasswordMail(String email, String token);
}
