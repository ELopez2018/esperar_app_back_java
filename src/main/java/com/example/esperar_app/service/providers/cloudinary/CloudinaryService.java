package com.example.esperar_app.service.providers.cloudinary;

import com.example.esperar_app.service.file.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService implements FileService {

    private static final Logger logger = LogManager.getLogger(CloudinaryService.class);

    @Override
    public String uploadImage(MultipartFile file) {
        logger.info("Uploading image to Cloudinary.");
        return null;
    }

    @Override
    public void deleteImage(String imageUrl) {
        logger.info("Deleting image from Cloudinary.");
    }
}
