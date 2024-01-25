package com.example.esperar_app.controller.notice;

import com.example.esperar_app.persistence.entity.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NoticeWebSocketController {

    @MessageMapping("/new-notice")
    @SendTo("/topic/new-notice")
    public Notice getNotice(@Payload Notice notice) {
        System.out.println("Hola desde el WebSocket");
        return notice;
    }

    @MessageMapping("/notices")
    @SendTo("/topic/notices")
    public Page<Notice> getNotices(@Payload Page<Notice> notices) {
        System.out.println("Solicitud de noticias recibida desde WebSocket");
        return notices;
    }

    @MessageMapping("/remove-notice")
    @SendTo("/topic/notice-removed")
    public String removeNotice(@Payload Long id) {
        return "Notice removed with id: " + id;
    }

}
