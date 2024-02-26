package com.example.esperar_app.controller.auth;

import com.example.esperar_app.persistence.dto.auth.AuthResponse;
import com.example.esperar_app.persistence.dto.auth.ChangePasswordDto;
import com.example.esperar_app.persistence.dto.auth.LogoutResponse;
import com.example.esperar_app.persistence.dto.user.CurrentUserDto;
import com.example.esperar_app.persistence.dto.user.LoginDto;
import com.example.esperar_app.service.auth.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * This method is used to authenticate the user
     * @param loginDto is the login data that contains the username and the password
     * @return the response entity that contains the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid LoginDto loginDto){
        logger.info("Login request received");

        AuthResponse authResponse = authService.login(loginDto);
        return ResponseEntity.ok(authResponse);

    }

    /**
     * This method is used to validate the access token
     * @param accessToken is the access token that is going to be validated
     * @return the response entity that contains the boolean value that indicates if the token is valid or not
     */
    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validate(@RequestParam String accessToken) {
        logger.info("Token validation request received");
        boolean isTokenValid = authService.validateToken(accessToken);
        return ResponseEntity.ok(isTokenValid);
    }

    /**
     * This method is used to get the current user
     * @return the response entity that contains the current user
     */
    @GetMapping("/current-user")
    public ResponseEntity<CurrentUserDto> getCurrentUser() {
        logger.info("Current user request received");
        CurrentUserDto currentUserDto = authService.getCurrentUser();
        return ResponseEntity.ok(currentUserDto);
    }

    /**
     * This method is used to log out the user
     * @param request is the request that contains the access token
     * @return the response entity that contains the logout response
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
        logger.info("Logout request received");
        authService.logout(request);
        return ResponseEntity.ok(new LogoutResponse("Logout successful"));
    }

    @PostMapping("/req-change-password")
    public ResponseEntity<String> sendEmailToChangePassword(
            @RequestParam String email
    ) {
        logger.info("User with email: [" + email + "] request change password request received");
        authService.sendEmailToChangePassword(email);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/change-password/{token}")
    public ResponseEntity<String> changePassword(
            @PathVariable String token,
            @RequestBody ChangePasswordDto changePasswordDto
    ) {
        logger.info("Change password request received");
        authService.changePassword(
                token,
                changePasswordDto.getOldPassword(),
                changePasswordDto.getNewPassword(),
                changePasswordDto.getConfirmPassword());


        return ResponseEntity.ok("Password changed successfully");
    }
}
