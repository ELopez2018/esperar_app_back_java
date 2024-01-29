package com.example.esperar_app.controller.auth;

import com.example.esperar_app.persistence.dto.inputs.user.LoginDto;
import com.example.esperar_app.persistence.dto.responses.auth.AuthResponse;
import com.example.esperar_app.persistence.dto.responses.auth.CurrentUserDto;
import com.example.esperar_app.persistence.dto.responses.auth.LogoutResponse;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.service.auth.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid LoginDto loginDto){

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
        boolean isTokenValid = authService.validateToken(accessToken);
        return ResponseEntity.ok(isTokenValid);
    }

    /**
     * This method is used to get the current user data
     * @return the response entity that contains the current user data
     */
    @GetMapping("/profile")
    public ResponseEntity<User> currentUser() {
        User user = authService.currentUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/current-user")
    public ResponseEntity<CurrentUserDto> getCurrentUser() {
        CurrentUserDto currentUserDto = authService.getCurrentUser();
        return ResponseEntity.ok(currentUserDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(new LogoutResponse("Logout successful"));
    }
}
