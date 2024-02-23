package com.example.esperar_app.service.auth.impl;

import com.example.esperar_app.exception.IncorrectPasswordException;
import com.example.esperar_app.exception.InvalidPasswordException;
import com.example.esperar_app.exception.InvalidTokenException;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * Validate the password pattern
     * @param password is the password that will be validated
     * @return true if the password is valid, false otherwise
     */
    public boolean validatePassword(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
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
     * Login a user
     * @param loginDto is the object that contains the user's credentials
     * @return the access token
     */
    public AuthResponse login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        Optional<User> user = userService.findOneByUsername(loginDto.getUsername());

        if(user.isPresent()) {
            String jwt = jwtService.generateToken(user.get(), generateExtraClaims(user.get()));

            saveUserAuth(user.get(), jwt);

            AuthResponse authRsp = new AuthResponse();
            authRsp.setAccessToken(jwt);
            authRsp.setId(user.get().getId());

            return authRsp;
        } else {
            throw new ObjectNotFoundException("User not found");
        }
    }

    /**
     * Validate a JWT token
     * @param accessToken is the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String accessToken) {
        try {
            jwtService.extractUsername(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Logout a user
     * @param request is the request that contains the JWT token
     */
    public void logout(HttpServletRequest request) {
        String accessToken = jwtService.extractJwtFromRequest(request);

        if(!StringUtils.hasText(accessToken)) return;

        Optional<UserAuth> userAuth = userAuthRepository.findByToken(accessToken);

        if(userAuth.isPresent() && userAuth.get().isValid()) {
            userAuth.get().setValid(false);
            userAuthRepository.save(userAuth.get());
        }
    }

    /**
     * Save the user's authentication
     * @param user is the user that will be saved
     * @param accessToken is the JWT token
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
     * Get the current user
     * @return the current user
     */
    public CurrentUserDto getCurrentUser() {
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
        if (user.getVehicle() != null) {
            Vehicle vehicle = user.getVehicle();
            GetVehicleDto vehicleDto = vehicleMapper.toGetVehicleDto(vehicle);
            currentUserDto.setCurrentVehicle(vehicleDto);
        }
    }

    /**
     * Email change the password
     * @param email is the email that will receive the email
     */
    @Transactional
    public void sendEmailToChangePassword(String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email [" + email + "] not found"));

        String encryptedToken = jwtService.encryptTokenToChangePassword(currentUser);

        currentUser.setChangePasswordToken(encryptedToken);

        try {
            userRepository.save(currentUser);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the token to change the password");
        }

        mailerService.sendChangePasswordMail(email, encryptedToken);

        System.out.println("Email sent to " + email + " with the token: " + encryptedToken);
    }

    private String encryptText(String text) {
        return passwordEncoder.encode(text);
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
        User currentUser = userRepository.findByChangePasswordToken(token)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (!validatePassword(newPassword)) {
            throw new InvalidPasswordException("The password does not meet the requirements");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordMismatchException("The new password and the confirmation password are not the same");
        }

        if (!jwtService.validateTokenToChangePassword(currentUser, token)) {
            throw new InvalidTokenException("The token is not valid");
        }

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new IncorrectPasswordException("The old password is not correct");
        }

        currentUser.setPassword(encryptText(newPassword));
        currentUser.setChangePasswordToken(null);

        try {
            userRepository.save(currentUser);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the new password");
        }
    }
}
