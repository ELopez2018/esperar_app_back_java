package com.example.esperar_app.service.providers.aws;

import com.example.esperar_app.service.file.FileService;
import com.example.esperar_app.service.providers.cloudinary.CloudinaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service implements FileService {

    private static final Logger logger = LogManager.getLogger(CloudinaryService.class);

    @Override
    public String uploadImage(MultipartFile file) {
        logger.info("Uploading image to S3.");
        return null;
    }

    @Override
    public void deleteImage(String imageUrl) {
        logger.info("Deleting image from S3.");
    }
}
