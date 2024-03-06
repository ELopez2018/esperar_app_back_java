package com.example.esperar_app.service.user;

import com.example.esperar_app.config.properties.ConfigProperties;
import com.example.esperar_app.exception.AlreadyExistError;
import com.example.esperar_app.exception.InvalidCloudProviderException;
import com.example.esperar_app.exception.InvalidPasswordException;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.exception.TermsAndConditionsException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.persistence.dto.user.CreateLegalPersonDto;
import com.example.esperar_app.persistence.dto.user.CreateNaturalPersonDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.user.RegisteredUser;
import com.example.esperar_app.persistence.dto.user.UpdateUserDto;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.persistence.utils.ImageType;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import com.example.esperar_app.persistence.utils.UserType;
import com.example.esperar_app.service.auth.RoleService;
import com.example.esperar_app.service.auth.impl.JwtService;
import com.example.esperar_app.service.file.providers.aws.S3Service;
import com.example.esperar_app.service.file.providers.cloudinary.CloudinaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final UserAuthRepository userAuthRepository;

    private final CloudinaryService cloudinaryService;

    private final S3Service s3Service;

    private final ConfigProperties configProperties;

    private final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleService roleService,
            UserMapper userMapper,
            JwtService jwtService,
            UserAuthRepository userAuthRepository,
            CloudinaryService cloudinaryService,
            S3Service s3Service,
            ConfigProperties configProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.userAuthRepository = userAuthRepository;
        this.cloudinaryService = cloudinaryService;
        this.s3Service = s3Service;
        this.configProperties = configProperties;
    }

    /**
     * Create a new user
     * @param createNaturalPersonDto is the object with the data to create the user
     * @return the user created
     */
    @Override
    public RegisteredUser create(CreateNaturalPersonDto createNaturalPersonDto) {
        if(!createNaturalPersonDto.getTermsAndConditions()) {
            logger.warn("Terms and conditions not accepted");
            throw new TermsAndConditionsException("Terms and conditions must be accepted");
        }

        validatePassword(createNaturalPersonDto.getPassword(), createNaturalPersonDto.getConfirmPassword());

        User user = userMapper.createUserDtoToUser(createNaturalPersonDto);

        validateAndSetDate(createNaturalPersonDto.getLicenseExpirationDate(), user);

        user.setPassword(encodePassword(createNaturalPersonDto.getPassword()));

        Role defaultRole = roleService.findDefaultRole()
                .orElseThrow(() -> new ObjectNotFoundException("Default role not found"));

        user.setRole(defaultRole);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setAcceptedTermsAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setFullName(userMapper.getFullName(user));
        user.setUserType(UserType.NATURAL_PERSON);

        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        try {
            user = userRepository.save(user);
            saveUserAuth(user, accessToken);

            RegisteredUser registeredUser = userMapper.toRegisteredUser(user);
            registeredUser.setAccessToken(accessToken);

            logger.info("User created successfully");

            return registeredUser;
        } catch (Exception e) {
            logger.error("Error creating user");
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Create a legal person
     * @param createLegalPersonDto is the object with the data to create the legal person
     * @return the legal person created
     */
    @Override
    public RegisteredUser createLegalPerson(CreateLegalPersonDto createLegalPersonDto) {
        String nit = createLegalPersonDto.getNit();
        String email = createLegalPersonDto.getEmail();
        String username = createLegalPersonDto.getUsername();

        List<User> usersFound = userRepository.findByUsernameOrEmailOrNit(username, email, nit);

        for (User existingUser : usersFound) {
            String duplicateField =
                    existingUser.getUsername().equals(username) ? "Username" :
                            existingUser.getEmail().equals(email) ? "Email" :
                                    existingUser.getNit().equals(nit) ? "NIT" : null;

            if (duplicateField != null) {
                logger.error("Error creating legal person: " + duplicateField + " already exists");
                throw new AlreadyExistError(duplicateField);
            }
        }

        if(!createLegalPersonDto.getTermsAndConditions()) {
            logger.warn("Terms and conditions not accepted");
            throw new TermsAndConditionsException("Terms and conditions must be accepted");
        }

        validatePassword(createLegalPersonDto.getPassword(), createLegalPersonDto.getConfirmPassword());

        User user = userMapper.createLegalPersonDtoToUser(createLegalPersonDto);

        user.setPassword(encodePassword(createLegalPersonDto.getPassword()));

        Role defaultRole = roleService.findDefaultRole()
                .orElseThrow(() -> new ObjectNotFoundException("Default role not found"));

        user.setRole(defaultRole);

        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setAcceptedTermsAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setUserType(UserType.LEGAL_PERSON);

        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        try {
            user = userRepository.save(user);
            saveUserAuth(user, accessToken);

            RegisteredUser registeredUser = userMapper.toRegisteredUser(user);
            registeredUser.setAccessToken(accessToken);

            logger.info("Legal person created successfully");

            return registeredUser;
        } catch (Exception e) {
            logger.error("Error creating legal person");
            throw new RuntimeException("Error creating legal person: " + e.getMessage());
        }
    }

    /**
     * Find a user by username
     * @param username is the username of the user to be found
     * @return the user found
     */
    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find all the users
     * @param pageable is the object with the pagination data
     * @return a page with the users found
     */
    @Override
    public Page<GetUserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toGetUserDto);
    }

    /**
     * Find a user by identifier
     * @param id is the id of the user to be found
     * @return the user found
     */
    @Override
    public GetUserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        return userMapper.toGetUserDto(user);
    }

    /**
     * Update a user by identifier
     * @param id is the id of the user to be updated
     * @param updateUserDto is the object with the new data
     * @return the updated user
     */
    @Override
    public GetUserDto update(Long id, UpdateUserDto updateUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        validateAndSetDate(updateUserDto.getLicenseExpirationDate(),
                existingUser
        );

        boolean namesChanged = !Objects.equals(existingUser.getFirstName(), updateUserDto.getFirstName()) ||
                !Objects.equals(existingUser.getSecondName(), updateUserDto.getSecondName()) ||
                !Objects.equals(existingUser.getLastName(), updateUserDto.getLastName());

        if (namesChanged) {
            String fullName = updateFullName(updateUserDto);
            existingUser.setFullName(fullName);
        }

        if(updateUserDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }

        BeanUtils.copyProperties(updateUserDto, existingUser, getNullPropertyNames(updateUserDto));

        try {
            userRepository.save(existingUser);
            logger.info("User updated successfully");
            return userMapper.toGetUserDto(existingUser);
        } catch (Exception e) {
            logger.error("Error updating user");
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }

    /**
     * Delete a user by identifier
     * @param id is the id of the user to be deleted
     */
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        try {
            userRepository.delete(user);
            logger.info("User deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting user");
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }

    /**
     * Connect the user to the chat
     * @param username is the username of the user to be connected
     * @return the user with the chat status updated
     */
    @Override
    public User connectUser(String username) {
        User userFound = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        userFound.setChatStatus(UserChatStatus.ONLINE);
        try {
            return userRepository.save(userFound);
        } catch (Exception e) {
            logger.error("Error connecting user to chat");
            throw new RuntimeException("Error connecting user to chat: " + e.getMessage());
        }
    }

    /**
     * Disconnect the user from the chat
     * @param username is the username of the user to be disconnected
     * @return the user with the chat status updated
     */
    @Override
    public User disconnectUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        user.setChatStatus(UserChatStatus.OFFLINE);

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error disconnecting user from chat");
            throw new RuntimeException("Error disconnecting user from chat: " + e.getMessage());
        }
    }

    /**
     * Find all the connected users
     * @return a list with the connected users
     */
    @Override
    public List<GetUserDto> findConnectedUsers() {
        List<User> connectedUsers = userRepository.findByChatStatus(UserChatStatus.ONLINE);

        return userMapper.toGetUserDtos(connectedUsers);
    }

    /**
     * Find all the drivers with the license soon to expire paginated
     * @param pageable is the object with the pagination data
     * @return a page with the drivers found
     */
    @Override
    public Page<GetUserDto> findDriversWithLicenseSoonToExpire(Pageable pageable) {
        Page<User> driversFound = userRepository.findDriversWithLicenseSoonToExpire(pageable);
        return driversFound.map(userMapper::toGetUserDto);
    }

    /**
     * Update the full name of the user
     * @param updateUserDto is the object with the new data
     * @return the new full name
     */
    private String updateFullName(UpdateUserDto updateUserDto) {
        return updateUserDto.getFirstName() + " " +
                (updateUserDto.getSecondName() != null ? updateUserDto.getSecondName() + " " : "") +
                updateUserDto.getLastName();
    }

    /**
     * Get the null property names from the source object
     * @param source is the object to get the null property names
     * @return an array with the null property names
     */
    private String[] getNullPropertyNames(Object source) {
        return getStrings(source);
    }

    /**
     * Validate the password and confirm password
     * @param password is the password to be validated
     * @param confirmPassword is the confirmation password to be validated
     */
    private void validatePassword(String password, String confirmPassword) {
        if(!StringUtils.hasText(password) || !StringUtils.hasText(confirmPassword)) {
            logger.error("Password and confirm password are required");
            throw new InvalidPasswordException("Password and confirm password are required");
        }

        if(!password.equals(confirmPassword)) {
            logger.error("Password and confirm password must match");
            throw new InvalidPasswordException("Password and confirm password must match");
        }
    }

    /**
     * Generate extra claims for the JWT token
     * @param user is the user that will be used to generate the claims
     * @return a map with the claims
     */
    private Map<String, Object> generateExtraClaims(User user) {
        return Map.of(
                "name", user.getFullName(),
                "role", user.getRole().getName(),
                "authorities", user.getAuthorities()
        );
    }

    /**
     * Save the user auth in the database
     * @param user is the user that will be used to generate the claims
     * @param accessToken is the token that will be saved
     */
    private void saveUserAuth(User user, String accessToken) {
        UserAuth userAuth = new UserAuth();
        userAuth.setToken(accessToken);
        userAuth.setUser(user);
        userAuth.setExpirationDate(jwtService.extractExpiration(accessToken));
        userAuth.setValid(true);

        try {
            userAuthRepository.save(userAuth);
        } catch (Exception e) {
            logger.error("Error saving user auth");
            throw new RuntimeException("Error saving user auth: " + e.getMessage());
        }
    }

    /**
     * Encode the password
     * @param password is the password to be encoded
     * @return the encoded password
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Validate and set the license expiration date
     * @param date is the date to be validated and set
     * @param user is the user that will be used to set the date
     */
    private void validateAndSetDate(String date, User user) {
        if(date == null) throw new RuntimeException("The password is required");

        if (DATE_PATTERN.matcher(date).matches() && isValidDate(date)) {
            logger.info("STEP 1");
            if(isValidDate(date)) {
                logger.info("STEP 2");
                user.setLicenseExpirationDate(date);
                logger.info("License expiration date set successfully");
            }
        } else {
            logger.error("Invalid license expiration date" + ", the correct format is dd-MM-yyyy");
            throw new IllegalArgumentException("Invalid license expiration date" + "," +
                    "the correct format is dd-MM-yyyy");
        }
    }

    /**
     * Validate the date
     * @param date is the date to be validated
     * @return a boolean indicating if the date is valid
     */
    private boolean isValidDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            simpleDateFormat.parse(date);
            return true;
        } catch (Exception e) {
            logger.warn("Invalid date format");
            return false;
        }
    }

    /**
     * Check the licenses to expire date
     */
    @Scheduled(cron = "0 50 9 * * *")
    public void checkLicensesToExpireDate() {
        Page<User> users = findUsersWithLicenseSoonToExpire();

        logger.info("Users with license soon to expire: " + users.getContent().size());

        for (User user : users) {
            logger.info("User: " + user.getUsername() + " - License expiration date: "
                    + user.getLicenseExpirationDate());
        }
    }

    /**
     * Find the users with the license soon to expire
     * @return a page with the users found
     */
    public Page<User> findUsersWithLicenseSoonToExpire() {
        Pageable pageable = Pageable.unpaged();
        return userRepository.findDriversWithLicenseSoonToExpire(pageable);
    }

    /**
     * Upload the chamber of commerce or driver license of the user to the cloud, depending on the cloud provider set in the properties
     * @param file is the file to be uploaded
     * @param userId is the id of the user that will be used to upload the file
     * @return a boolean indicating if the file was uploaded successfully
     */
    public Boolean uploadUserDocument(
            MultipartFile file,
            Long userId,
            ImageType imageType) {
        ConfigProperties.CloudPlatform cloudPlatform = configProperties.cloudPlatform();

        String provider = cloudPlatform.provider();
        logger.info("Provider: " + provider);

        switch (provider) {
            case "cloudinary":
                cloudinaryService.uploadUserDocument(file, userId, imageType);
                break;
            case "s3":
                s3Service.uploadUserDocument(file, userId, imageType);
                break;
            default:
                throw new InvalidCloudProviderException("Invalid service provider: " + provider);
        }

        return true;
    }
}
