package com.example.esperar_app.service.user;

import com.example.esperar_app.exception.AlreadyExistError;
import com.example.esperar_app.exception.InvalidPasswordException;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.exception.TermsAndConditionsException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.persistence.dto.user.CreateLegalPersonDto;
import com.example.esperar_app.persistence.dto.user.CreateUserDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.user.RegisteredUser;
import com.example.esperar_app.persistence.dto.user.UpdateUserDto;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import com.example.esperar_app.persistence.utils.UserType;
import com.example.esperar_app.service.auth.RoleService;
import com.example.esperar_app.service.auth.impl.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final UserAuthRepository userAuthRepository;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN, Pattern.CASE_INSENSITIVE);

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleService roleService,
            UserMapper userMapper,
            JwtService jwtService,
            UserAuthRepository userAuthRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.userAuthRepository = userAuthRepository;
    }

    /**
     * Create a new user
     * @param createUserDto is the object with the data to create the user
     * @return the user created
     */
    @Override
    public RegisteredUser create(CreateUserDto createUserDto) {
        if(!createUserDto.getTermsAndConditions()) {
            throw new TermsAndConditionsException("Terms and conditions must be accepted");
        }

        validatePassword(createUserDto.getPassword(), createUserDto.getConfirmPassword());

//        if (!validatePasswordRegex(createUserDto.getPassword())) {
//            throw new InvalidPasswordException("Password must have at least 8 characters," +
//                    "1 uppercase letter, 1 lowercase letter, 1 number and 1 special character");
//        }


        User user = userMapper.createUserDtoToUser(createUserDto);

        user.setPassword(encodePassword(createUserDto.getPassword()));

        Role defaultRole = roleService.findDefaultRole()
                .orElseThrow(() -> new ObjectNotFoundException("Default role not found"));

        user.setRole(defaultRole);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setAcceptedTermsAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setFullName(userMapper.getFullName(user));
        user.setUserType(UserType.NATURAL_PERSON);

        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        user = userRepository.save(user);

        saveUserAuth(user, accessToken);

        RegisteredUser registeredUser = userMapper.toRegisteredUser(user);
        registeredUser.setAccessToken(accessToken);
        return registeredUser;
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
        String password = createLegalPersonDto.getPassword();

//        if(!validatePasswordRegex(password)) {
//            throw new InvalidPasswordException("Password must have at least 8 characters," +
//                    "1 uppercase letter, 1 lowercase letter, 1 number and 1 special character");
//        }

        List<User> usersFound = userRepository.findByUsernameOrEmailOrNit(username, email, nit);

        for (User existingUser : usersFound) {
            String duplicateField =
                    existingUser.getUsername().equals(username) ? "Username" :
                            existingUser.getEmail().equals(email) ? "Email" :
                                    existingUser.getNit().equals(nit) ? "NIT" : null;

            if (duplicateField != null) {
                throw new AlreadyExistError(duplicateField);
            }
        }

        if(!createLegalPersonDto.getTermsAndConditions()) {
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

        user = userRepository.save(user);

        saveUserAuth(user, accessToken);

        RegisteredUser registeredUser = userMapper.toRegisteredUser(user);
        registeredUser.setAccessToken(accessToken);

        return registeredUser;
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

        String password = updateUserDto.getPassword();

//        if(!validatePasswordRegex(password)) {
//            throw new InvalidPasswordException("Password must have at least 8 characters," +
//                    "1 uppercase letter, 1 lowercase letter, 1 number and 1 special character");
//        }

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

        userRepository.save(existingUser);

        return userMapper.toGetUserDto(existingUser);
    }

    /**
     * Delete a user by identifier
     * @param id is the id of the user to be deleted
     */
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        userRepository.delete(user);
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
        return userRepository.save(userFound);
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
        return userRepository.save(user);
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
            throw new InvalidPasswordException("Password and confirm password are required");
        }

        if(!password.equals(confirmPassword)) {
            throw new InvalidPasswordException("Password and confirm password must match");
        }
    }

    /**
     * Generate extra claims for the JWT token
     * @param user is the user that will be used to generate the claims
     * @return a map with the claims
     */
    public Map<String, Object> generateExtraClaims(User user) {
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
    public void saveUserAuth(User user, String accessToken) {
        UserAuth userAuth = new UserAuth();
        userAuth.setToken(accessToken);
        userAuth.setUser(user);
        userAuth.setExpirationDate(jwtService.extractExpiration(accessToken));
        userAuth.setValid(true);

        userAuthRepository.save(userAuth);
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
     * Validate the password pattern
     * @param password is the password that will be validated
     * @return true if the password is valid, false otherwise
     */
    public boolean validatePasswordRegex(String password) {
        Matcher matcher = pattern.matcher(password);
        System.out.println(matcher.matches());
        return matcher.matches();
    }

}
