package com.example.esperar_app.service.notice;

import com.example.esperar_app.persistence.dto.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.dto.notice.GetNoticeDto;
import com.example.esperar_app.persistence.entity.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    GetNoticeDto create(CreateNoticeDto createNoticeDto);

    Page<Notice> findAll(Pageable pageable);

    Notice findById(Long id);

    void remove(Long id);
}
