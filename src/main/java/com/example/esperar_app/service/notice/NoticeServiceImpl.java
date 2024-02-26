package com.example.esperar_app.service.notice;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.NoticeMapper;
import com.example.esperar_app.persistence.dto.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.dto.notice.GetNoticeDto;
import com.example.esperar_app.persistence.entity.notice.Notice;
import com.example.esperar_app.persistence.repository.NoticeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NoticeMapper noticeMapper;
    private final NoticeRepository noticeRepository;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public NoticeServiceImpl(
            SimpMessagingTemplate messagingTemplate,
            NoticeMapper noticeMapper,
            NoticeRepository noticeRepository) {
        this.messagingTemplate = messagingTemplate;
        this.noticeMapper = noticeMapper;
        this.noticeRepository = noticeRepository;
    }

    @Override
    public GetNoticeDto create(CreateNoticeDto createNoticeDto) {
        Notice newNotice = noticeMapper.toNotice(createNoticeDto);
        newNotice.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        try {
            newNotice = noticeRepository.save(newNotice);
        } catch (Exception e) {
            logger.error("Error creating notice");
            throw e;
        }

        return noticeMapper.getNoticeDtoToNotice(newNotice);
    }

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAll(pageable);
        messagingTemplate.convertAndSend("/topic/notices", notices);
        logger.info("Notices returned successfully.");
        return notices;
    }

    @Override
    public Notice findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Notice not found with id " + id));

        messagingTemplate.convertAndSend("/topic/notice", notice);
        logger.info("Notice returned successfully.");
        return notice;
    }

    @Override
    public void remove(Long id) {
        Notice noticeFound = findById(id);
        try {
            messagingTemplate.convertAndSend("/topic/notice-removed", noticeFound.getId());
            noticeRepository.delete(noticeFound);
            logger.info("Notice removed successfully.");
        } catch (Exception e) {
            logger.error("Error removing notice");
            throw e;
        }
    }

}
