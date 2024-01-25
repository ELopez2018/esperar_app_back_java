package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> { }