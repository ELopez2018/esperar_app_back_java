package com.example.esperar_app.service.notice;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.NoticeMapper;
import com.example.esperar_app.persistence.dto.inputs.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.entity.notice.Notice;
import com.example.esperar_app.persistence.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NoticeMapper noticeMapper;
    private final NoticeRepository noticeRepository;

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
    public Notice create(CreateNoticeDto createNoticeDto) {
        try {
            Notice newNotice = noticeMapper.toNotice(createNoticeDto);

            LocalDateTime myDateObj = LocalDateTime.now();
            Instant instant = myDateObj.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);

            newNotice.setCreatedAt(date);

            System.out.println("FECHA: " + newNotice.getCreatedAt());

            newNotice = noticeRepository.save(newNotice);

            return newNotice;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAll(pageable);
        messagingTemplate.convertAndSend("/topic/notices", notices);
        return notices;
    }

    @Override
    public Notice findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Notice not found with id " + id));

        messagingTemplate.convertAndSend("/topic/notice", notice);

        return notice;
    }

    @Override
    public void remove(Long id) {
        Notice noticeFound = findById(id);
        try {
            messagingTemplate.convertAndSend("/topic/notice-removed", noticeFound.getId());
            noticeRepository.delete(noticeFound);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

}
