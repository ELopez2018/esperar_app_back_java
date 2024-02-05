package com.example.esperar_app.controller.notice;

import com.example.esperar_app.persistence.dto.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.entity.notice.Notice;
import com.example.esperar_app.service.notice.NoticeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NoticeWebSocketController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeWebSocketController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @MessageMapping("/createNews")
    @SendTo("/topic/news")
    public Notice create(@Payload @Valid CreateNoticeDto createNoticeDto) {
        return noticeService.create(createNoticeDto);
    }

    @MessageMapping("/deleteNews")
    @SendTo("/topic/news")
    public Map<String, Object> delete(@Payload Long newsId) {
        noticeService.remove(newsId);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedNewsId", newsId);
        return response;
    }

}
