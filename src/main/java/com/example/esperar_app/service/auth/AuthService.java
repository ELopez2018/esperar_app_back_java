package com.example.esperar_app.service.auth;

import com.example.esperar_app.dto.inputs.CreateUserDto;
import com.example.esperar_app.dto.inputs.LoginDto;
import com.example.esperar_app.dto.inputs.RegisteredUser;
import com.example.esperar_app.dto.responses.AuthResponse;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAuthRepository userAuthRepository;

    @Autowired
    public AuthService(
            UserService userService,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserAuthRepository userAuthRepository) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userAuthRepository = userAuthRepository;
    }

    /**
     * Create a new user (sign up)
     * @param createUserDto is the object that contains the user's data
     * @return the registered user
     */
    public RegisteredUser registerOne(CreateUserDto createUserDto) {
        User user = userService.create(createUserDto);
        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        saveUserAuth(user, accessToken);

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setRole(user.getRole().getName());
        registeredUser.setAccessToken(accessToken);

        BeanUtils.copyProperties(user, registeredUser);

        return registeredUser;
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
     * Login a user
     * @param loginDto is the object that contains the user's credentials
     * @return the access token
     */
    public AuthResponse login(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        User user = userService.findOneByUsername(loginDto.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        saveUserAuth(user, accessToken);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setId(user.getId());

        return authResponse;
    }

    /**
     * Validate a JWT token
     * @param accessToken is the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String accessToken) {
        try {
            String username = jwtService.extractUsername(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the current user
     * @return the current user
     */
    public User currentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
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

    private void saveUserAuth(User user, String accessToken) {
        UserAuth userAuth = new UserAuth();
        userAuth.setToken(accessToken);
        userAuth.setUser(user);
        userAuth.setExpirationDate(jwtService.extractExpiration(accessToken));
        userAuth.setValid(true);

        userAuthRepository.save(userAuth);
    }

}
