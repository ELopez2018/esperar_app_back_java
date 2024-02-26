package com.example.esperar_app.persistence.dto.user;

import com.example.esperar_app.persistence.utils.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationDocumentDto {
    private String documentNumber;

    private DocumentType documentType;
}
