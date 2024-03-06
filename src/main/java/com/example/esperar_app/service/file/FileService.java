package com.example.esperar_app.service.file;

import com.example.esperar_app.persistence.utils.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void uploadUserDocument(MultipartFile file, Long userId, ImageType imageType);
    void uploadVehicleDocument(MultipartFile file, Long vehicleId, ImageType imageType);
}
