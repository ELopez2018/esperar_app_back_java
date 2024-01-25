package com.example.esperar_app.controller.notice;

import com.example.esperar_app.persistence.dto.inputs.notice.CreateNoticeDto;
import com.example.esperar_app.persistence.entity.notice.Notice;
import com.example.esperar_app.service.notice.NoticeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public ResponseEntity<Notice> create(
            @RequestBody @Valid CreateNoticeDto createNoticeDto) {
        Notice notice = noticeService.create(createNoticeDto);
        if(notice == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(notice);
    }

    @GetMapping
    public ResponseEntity<Page<Notice>> findAll(Pageable pageable) {
        Page<Notice> notices = noticeService.findAll(pageable);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("{id}")
    public ResponseEntity<Notice> findById(@PathVariable Long id) {
        Notice notice = noticeService.findById(id);
        if(notice == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(notice);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        noticeService.remove(id);
        return ResponseEntity.ok().build();
    }
}
