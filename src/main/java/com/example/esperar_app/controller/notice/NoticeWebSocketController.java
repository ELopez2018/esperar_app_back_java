package com.example.esperar_app.controller.notice;

import com.example.esperar_app.config.websocket.WebSocketConfig;
import com.example.esperar_app.persistence.dto.inputs.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.entity.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //* PRUEBAS
    @MessageMapping("/createNews")
    @SendTo("/topic/news")
    public Notice createNews(@Payload CreateNoticeDto createNoticeDto) {
        System.out.println("Hola desde el WebSocket");

        Date now = new Date(System.currentTimeMillis());

        Notice notice = new Notice();
        notice.setTitle(createNoticeDto.getTitle());
        notice.setContent(createNoticeDto.getContent());
        notice.setCreatedAt(now);

        return notice;
    }


}
