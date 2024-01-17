package com.example.esperar_app.config.security.handler;

import com.example.esperar_app.persistence.dto.responses.auth.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        ApiError error = new ApiError();
        error.setMessage("Access denied, your not have permission to access this resource," +
                "please contact the administrator if you think this is a mistake");
        error.setBackedMessage(accessDeniedException.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(403);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String errorJson = mapper.writeValueAsString(error);

        response.getWriter().write(errorJson);
    }
}
