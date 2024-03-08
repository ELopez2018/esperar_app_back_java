package com.example.esperar_app.service.file.providers.cloudinary;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.repository.VehicleRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.persistence.utils.ImageType;
import com.example.esperar_app.service.file.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
public class CloudinaryService implements FileService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public CloudinaryService(
            UserRepository userRepository,
            VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    private static final Logger logger = LogManager.getLogger(CloudinaryService.class);

    @Override
    @Transactional
    public void uploadUserDocument(
            MultipartFile file,
            Long userId,
            ImageType imageType
    ) {
        logger.info("Trying uploading image to storage.");

        User user = getUserById(userId);

        switch (imageType) {
            case CHAMBER_OF_COMMERCE:
                updateEntityDocumentFields(
                        (Timestamp) user.getChamberOfCommerceCreatedAt(),
                        user::setChamberOfCommerceCreatedAt,
                        user::setChamberOfCommerceUpdatedAt);

                user.setChamberOfCommerceUrl("https://cloudinary.com/esperar-app/" + file.getOriginalFilename());
                break;
            case DRIVER_LICENSE:
                updateEntityDocumentFields(
                        (Timestamp) user.getDriverLicenseCreatedAt(),
                        user::setDriverLicenseCreatedAt,
                        user::setDriverLicenseUpdatedAt);

                user.setDriverLicenseUrl("https://s3.amazonaws.com/esperar-app/" + file.getOriginalFilename());
                break;
        }

        userRepository.save(user);

        logger.info("Image uploaded to storage.");
    }

    @Override
    public void uploadVehicleDocument(
            MultipartFile file,
            Long vehicleId,
            ImageType imageType
    ) {
        logger.info("Trying uploading image to storage.");

        Vehicle vehicle = getVehicleById(vehicleId);

        switch (imageType) {
            case VEHICLE_TECHNICAL_REVISION:
                updateEntityDocumentFields(
                        (Timestamp) vehicle.getTecnoMechanicalCreatedAt(),
                        vehicle::setTecnoMechanicalCreatedAt,
                        vehicle::setTecnoMechanicalUpdatedAt);

                vehicle.setTecnoMechanicalUrl("https://cloudinary.com/esperar-app/" + file.getOriginalFilename());
                break;
            case VEHICLE_SOAT:
                updateEntityDocumentFields(
                        (Timestamp) vehicle.getSoatCreatedAt(),
                        vehicle::setSoatCreatedAt,
                        vehicle::setSoatUpdatedAt);

                vehicle.setSoatUrl("https://s3.amazonaws.com/esperar-app/" + file.getOriginalFilename());
                break;
        }

        vehicleRepository.save(vehicle);

        logger.info("Image uploaded to storage.");
    }

    /**
     * Update user image fields.
     * @param createdAt created at
     * @param createSetter create setter
     * @param updateSetter update setter
     */
    private void updateEntityDocumentFields(
            Timestamp createdAt,
            Consumer<Timestamp> createSetter,
            Consumer<Timestamp> updateSetter) {
        if (createdAt != null) {
            logger.info("Entity already has an document uploaded, updating it.");
            updateSetter.accept(Timestamp.valueOf(LocalDateTime.now()));
        } else {
            logger.info("Entity doesn't have an document uploaded, creating it.");
            createSetter.accept(Timestamp.valueOf(LocalDateTime.now()));
        }
    }

    /**
     * Get user by identifier.
     * @param userId user identifier
     * @return user
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + userId + " not found."));
    }

    /**
     * Get vehicle by identifier.
     * @param vehicleId vehicle identifier
     * @return vehicle
     */
    private Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle with id " + vehicleId + " not found."));
    }
}
