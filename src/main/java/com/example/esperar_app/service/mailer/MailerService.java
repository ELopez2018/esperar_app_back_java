package com.example.esperar_app.service.mailer;

public interface MailerService {
    String sendMail();

    String sendChangePasswordMail(String email, String token);
}
