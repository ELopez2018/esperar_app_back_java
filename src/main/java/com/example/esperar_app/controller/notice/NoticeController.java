package com.example.esperar_app.controller.notice;

import com.example.esperar_app.persistence.entity.notice.Notice;
import com.example.esperar_app.service.notice.NoticeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController()
@RequestMapping("/notices")
public class NoticeController {
    private static final Logger logger = LogManager.getLogger();

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

//    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
//    public ResponseEntity<Notice> create(
//            @RequestBody @Valid CreateNoticeDto createNoticeDto) {
//        Notice notice = noticeService.create(createNoticeDto);
//        if(notice == null) return ResponseEntity.badRequest().build();
//        return ResponseEntity.ok(notice);
//    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Page<Notice>> findAll(Pageable pageable) {
        logger.info("Find all notices request received.");
        Page<Notice> notices = noticeService.findAll(pageable);
        return ResponseEntity.ok(notices != null ? notices : Page.empty());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Notice> findById(@PathVariable Long id) {
        logger.info("Find notice with id: [" + id + "] request received.");
        Notice notice = noticeService.findById(id);
        return ResponseEntity.of(Optional.ofNullable(notice));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        logger.info("Remove notice with id: [" + id + "] request received.");
        noticeService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
