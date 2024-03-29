package com.example.esperar_app.persistence.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GetNoticeDto {
    private Long id;

    private String title;

    private String content;

    private Date createdAt;
}
