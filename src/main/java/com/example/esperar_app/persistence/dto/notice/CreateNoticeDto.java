package com.example.esperar_app.persistence.dto.inputs.notice;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNoticeDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;
}
