package com.example.esperar_app.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(MultipartFile file);
    void deleteImage(String imageUrl);
}
