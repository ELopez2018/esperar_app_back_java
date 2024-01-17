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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAuthRepository userAuthRepository;

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

        System.out.println("Username: " + loginDto.getUsername());
        System.out.println("Password: " + loginDto.getPassword());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            System.out.println("Pasamos?");

            Optional<User> user = userService.findOneByUsername(loginDto.getUsername());

            if(user.isPresent()) {
                System.out.println("User found: " + user);

                String jwt = jwtService.generateToken(user.get(), generateExtraClaims(user.get()));

                saveUserAuth(user.get(), jwt);

                AuthResponse authRsp = new AuthResponse();
                authRsp.setAccessToken(jwt);
                authRsp.setId(user.get().getId());

                System.out.println("User authenticated: " + loginDto.getUsername());

                return authRsp;
            } else {
                System.out.println("User not found: " + loginDto.getUsername());
                throw new ObjectNotFoundException("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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

    public void saveUserAuth(User user, String accessToken) {
        UserAuth userAuth = new UserAuth();
        userAuth.setToken(accessToken);
        userAuth.setUser(user);
        userAuth.setExpirationDate(jwtService.extractExpiration(accessToken));
        userAuth.setValid(true);

        userAuthRepository.save(userAuth);
    }

}
