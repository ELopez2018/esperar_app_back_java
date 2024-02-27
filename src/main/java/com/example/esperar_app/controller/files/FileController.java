package com.example.esperar_app.controller.files;

import com.example.esperar_app.config.properties.ConfigProperties;
import com.example.esperar_app.exception.InvalidCloudProviderException;
import com.example.esperar_app.service.providers.aws.S3Service;
import com.example.esperar_app.service.providers.cloudinary.CloudinaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    private final CloudinaryService cloudinaryService;
    private final S3Service s3Service;
    private final ConfigProperties configProperties;
    private static final Logger logger = LogManager.getLogger(FileController.class);

    @Autowired
    public FileController(
            CloudinaryService cloudinaryService,
            S3Service s3Service,
            ConfigProperties configProperties) {
        this.cloudinaryService = cloudinaryService;
        this.s3Service = s3Service;
        this.configProperties = configProperties;
    }

    // TODO: Upload image to cloud service

    /**
     * Uploads the companie's chamber of commerce to the cloud service
     * @param file The file to be uploaded
     * @param userId The user type company's id
     * @return A response entity with a message
     */
    @PostMapping("/single/upload/chamber-of-commerce")
    public ResponseEntity<String> uploadChamberOfCommerce(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        logger.info("File uploading request received.");
        logger.info("The file original name is: " + file.getOriginalFilename());
        logger.info("User id is: " + userId);

        ConfigProperties.CloudPlatform cloudPlatform = configProperties.cloudPlatform();

        String provider = cloudPlatform.provider();
        logger.info("Provider: " + provider);

        switch (provider) {
            case "cloudinary":
                cloudinaryService.uploadImage(file);
                break;
            case "s3":
                s3Service.uploadImage(file);
                break;
            default:
                throw new InvalidCloudProviderException("Invalid service provider: " + provider);
        }

        return ResponseEntity.ok("Chamber of Commerce successfully uploaded");
    }
}
