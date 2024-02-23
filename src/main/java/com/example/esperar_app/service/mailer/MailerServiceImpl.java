package com.example.esperar_app.service.mailer;

import com.example.esperar_app.config.properties.ConfigProperties;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailerServiceImpl implements MailerService {

    private final ConfigProperties configProperties;

    @Autowired
    public MailerServiceImpl(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public String sendMail() {
        System.out.println("Estamos en el mailer service");

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
            System.out.println(data.getId());
            return "Success";
        } catch (ResendException e) {
            e.printStackTrace();
        }

        return "Error, check logs for more information.";
    }

    @Override
    public String sendChangePasswordMail(String email, String token) {
        ConfigProperties.Resend resendEnvironments = configProperties.resend();

        Resend resend = new Resend(resendEnvironments.apiKey());

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Acme <onboarding@resend.dev>")
                .to("juanescalderon12@gmail.com")
                .subject("it works!")
                .html("You can change your password here:" +
                        "<a href=\"http://localhost:8000/api/v1/auth/change-password/" + token + "\">Click here</a>")
                .build();

        try {
            resend.emails().send(sendEmailRequest);
            return "Success";
        } catch (ResendException e) {
            e.printStackTrace();
        }

        return "Error, check logs for more information.";
    }
}
