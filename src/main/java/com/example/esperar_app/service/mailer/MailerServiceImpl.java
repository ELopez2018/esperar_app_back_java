package com.example.esperar_app.service.mailer;

import com.example.esperar_app.config.properties.ConfigProperties;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailerServiceImpl implements MailerService {

    private final ConfigProperties configProperties;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public MailerServiceImpl(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public String sendMail() {
        logger.info("Sending welcome email");

        ConfigProperties.Resend resendEnvironments = configProperties.resend();

        Resend resend = new Resend(resendEnvironments.apiKey());

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Acme <onboarding@resend.dev>")
                .to("juanescalderon12@gmail.com")
                .subject("it works!")
                .html("<strong>hello world</strong>")
                .build();

        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            logger.info("Email identifier: " + data.getId());
            return "Success";
        } catch (ResendException e) {
            logger.error("Error sending email");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendChangePasswordMail(String email, String token) {
        logger.info("Sending email to change password");
        ConfigProperties.Resend resendEnvironments = configProperties.resend();

        Resend resend = new Resend(resendEnvironments.apiKey());

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Acme <onboarding@resend.dev>")
                .to("juanescalderon12@gmail.com")
                .subject("it works!")
                .html("You can change your password here:" +
                        "<a href=\"http://localhost:8080/api/v1/auth/change-password/" + token + "\">Click here</a>")
                .build();

        try {
            SendEmailResponse response = resend.emails().send(sendEmailRequest);
            logger.info("Email identifier: " + response.getId() + " sent successfully.");
        } catch (ResendException e) {
            logger.error("Error sending email");
            throw new RuntimeException(e);
        }
    }
}
