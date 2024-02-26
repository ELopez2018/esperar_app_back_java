package com.example.esperar_app.controller.notice;

import com.example.esperar_app.persistence.dto.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.dto.notice.GetNoticeDto;
import com.example.esperar_app.service.notice.NoticeService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public NoticeWebSocketController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @MessageMapping("/createNews")
    @SendTo("/topic/news")
    public GetNoticeDto create(@Payload @Valid CreateNoticeDto createNoticeDto) {
        logger.info("Create a new notice request received.");
        return noticeService.create(createNoticeDto);
    }

    @MessageMapping("/deleteNews")
    @SendTo("/topic/news")
    public Map<String, Object> delete(@Payload Long newsId) {
        logger.info("Delete a notice with id: [" + newsId + "] request received.");
        noticeService.remove(newsId);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedNewsId", newsId);
        return response;
    }

}
