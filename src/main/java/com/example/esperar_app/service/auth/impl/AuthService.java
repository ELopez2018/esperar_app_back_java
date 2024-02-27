package com.example.esperar_app.service.auth.impl;

import com.example.esperar_app.exception.IncorrectPasswordException;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.exception.PasswordMismatchException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.mapper.VehicleMapper;
import com.example.esperar_app.persistence.dto.auth.AuthResponse;
import com.example.esperar_app.persistence.dto.user.CurrentUserDto;
import com.example.esperar_app.persistence.dto.user.LoginDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.service.mailer.MailerService;
import com.example.esperar_app.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserAuthRepository userAuthRepository;

    private final UserMapper userMapper;

    private final VehicleMapper vehicleMapper;

    private final UserRepository userRepository;

    private final MailerService mailerService;

    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LogManager.getLogger();

    /**
     * Generate extra claims for the JWT token
     * @param user is the user that will be used to generate the claims
     * @return a map with the claims
     */
    public Map<String, Object> generateExtraClaims(User user) {
        logger.info("Generating extra claims for the JWT token");
        return Map.of(
                "name", user.getFullName(),
                "role", user.getRole().getName(),
                "authorities", user.getAuthorities()
        );
    }

    /**
     * Login a user
     * @param loginDto is the object that contains the user's credentials
     * @return the access token
     */
    public AuthResponse login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (Exception e) {
            logger.error("Invalid username or password");
            throw new RuntimeException("Invalid username or password");
        }

        Optional<User> user = userService.findOneByUsername(loginDto.getUsername());

        if(user.isPresent()) {
            String jwt = jwtService.generateToken(user.get(), generateExtraClaims(user.get()));

            saveUserAuth(user.get(), jwt);

            AuthResponse authRsp = new AuthResponse();
            authRsp.setAccessToken(jwt);
            authRsp.setId(user.get().getId());

            return authRsp;
        } else {
            logger.error("User not found");
            throw new ObjectNotFoundException("User not found");
        }
    }

    /**
     * Validate a JWT token
     * @param accessToken is the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String accessToken) {
        logger.info("Validating JWT token");
        try {
            jwtService.extractUsername(accessToken);
            logger.info("JWT token is valid");
            return true;
        } catch (Exception e) {
            logger.error("JWT token is not valid");
            return false;
        }
    }

    /**
     * Logout a user
     * @param request is the request that contains the JWT token
     */
    public void logout(HttpServletRequest request) {
        logger.info("Logging out user");
        String accessToken = jwtService.extractJwtFromRequest(request);

        if(!StringUtils.hasText(accessToken)) {
            logger.error("JWT token not received");
            return;
        }

        Optional<UserAuth> userAuth = userAuthRepository.findByToken(accessToken);

        if(userAuth.isPresent() && userAuth.get().isValid()) {
            userAuth.get().setValid(false);
            userAuthRepository.save(userAuth.get());
            logger.info("User logged out");
        }
    }

    /**
     * Save the user's authentication
     * @param user is the user that will be saved
     * @param accessToken is the JWT token
     */
    public void saveUserAuth(User user, String accessToken) {
        logger.info("Saving user authentication");
        UserAuth userAuth = new UserAuth();
        userAuth.setToken(accessToken);
        userAuth.setUser(user);
        userAuth.setExpirationDate(jwtService.extractExpiration(accessToken));
        userAuth.setValid(true);

        try {
            userAuthRepository.save(userAuth);
            logger.info("User authentication saved");
        } catch (Exception e) {
            logger.error("Error saving user authentication");
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get the current user
     * @return the current user
     */
    public CurrentUserDto getCurrentUser() {
        logger.info("Getting current user");

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userFound = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        CurrentUserDto currentUserDtoMapper = userMapper.toCurrentUserDto(userFound);

        setVehicleInfo(userFound, currentUserDtoMapper);

        return currentUserDtoMapper;
    }

    /**
     * Set the vehicle info to the current user
     * @param user is the user
     * @param currentUserDto is the current user
     */
    private void setVehicleInfo(User user, CurrentUserDto currentUserDto) {
        logger.info("Setting vehicle info to the current user");
        if (user.getVehicle() != null) {
            Vehicle vehicle = user.getVehicle();
            GetVehicleDto vehicleDto = vehicleMapper.toGetVehicleDto(vehicle);
            currentUserDto.setCurrentVehicle(vehicleDto);
            logger.info("Vehicle info set to the current user");
        }
    }

    /**
     * Email change the password
     * @param email is the email that will receive the email
     */
    @Transactional
    public void sendEmailToChangePassword(String email) {
        logger.info("Sending email to change password to [" + email + "]");

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email [" + email + "] not found"));

        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();

        currentUser.setChangePasswordToken(token);

        try {
            userRepository.save(currentUser);
            logger.info("Token to change password saved");
        } catch (Exception e) {
            logger.error("Error saving the token to change the password");
            throw new RuntimeException("Error saving the token to change the password");
        }

        mailerService.sendChangePasswordMail(email, token);

        logger.info("Email to change password sent");
    }

    /**
     * Change the password
     * @param token is the token that will be used to change the password
     * @param oldPassword is the old password
     * @param newPassword is the new password
     * @param confirmPassword is the confirmation password
     */
    public void changePassword(
            String token,
            String oldPassword,
            String newPassword,
            String confirmPassword
    ) {
        logger.info("Changing password");

        User currentUser = userRepository.findByChangePasswordToken(token)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (!newPassword.equals(confirmPassword)) {
            logger.error("The new password and the confirmation password are not the same");
            throw new PasswordMismatchException("The new password and the confirmation password are not the same");
        }

        if(oldPassword != null) {
            if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                logger.error("The old password is not correct");
                throw new IncorrectPasswordException("The old password is not correct");
            }
        }

        currentUser.setPassword(encryptText(newPassword));
        currentUser.setChangePasswordToken(null);

        try {
            userRepository.save(currentUser);
            logger.info("Password changed");
        } catch (Exception e) {
            logger.error("Error saving the new password");
            throw new RuntimeException("Error saving the new password");
        }
    }

    /**
     * Encrypt any text
     * @param text is the text that will be encrypted
     * @return the encrypted text
     */
    private String encryptText(String text) {
        logger.info("Encrypting text");
        return passwordEncoder.encode(text);
    }
}
