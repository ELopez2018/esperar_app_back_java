package com.example.esperar_app.service.auth.impl;

import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.mapper.VehicleMapper;
import com.example.esperar_app.persistence.dto.auth.AuthResponse;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.dto.user.CurrentUserDto;
import com.example.esperar_app.persistence.dto.user.LoginDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserAuthRepository userAuthRepository;

    private final UserMapper userMapper;

    private final VehicleMapper vehicleMapper;

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
}
