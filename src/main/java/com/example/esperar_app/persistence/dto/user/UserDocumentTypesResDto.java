package com.example.esperar_app.persistence.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocumentTypesResDto {
    private List<DocumentTypesObject> documentTypesObjects;
}
