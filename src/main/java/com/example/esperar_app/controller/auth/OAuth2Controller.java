package com.example.esperar_app.controller.auth;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("oauth2")
public class OAuth2Controller {

    @GetMapping
    public Map<String, Object> currentUser(OAuth2AuthenticationToken oAuth2Token) {
        System.out.println("OAuth2Controller.currentUser");
        System.out.println(oAuth2Token);
        try {
            return oAuth2Token.getPrincipal().getAttributes();
        } catch (Exception e) {
            return Collections.singletonMap("error",
                    "The error is: " + e.getMessage() + " and the cause is: " + e.getCause());
        }
    }
}
