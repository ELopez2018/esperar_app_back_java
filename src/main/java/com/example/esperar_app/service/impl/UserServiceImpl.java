package com.example.esperar_app.service.impl;

import com.example.esperar_app.dto.inputs.CreateUserDto;
import com.example.esperar_app.dto.inputs.RegisteredUser;
import com.example.esperar_app.dto.responses.GetUser;
import com.example.esperar_app.exception.InvalidPasswordException;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.service.RoleService;
import com.example.esperar_app.service.UserService;
import com.example.esperar_app.service.auth.JwtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserAuthRepository userAuthRepository;

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
    @Override
    public RegisteredUser create(CreateUserDto createUserDto) {
        validatePassword(createUserDto);

        User user = userMapper.createUserDtoToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        Role defaultRole = roleService.findDefaultRole()
                        .orElseThrow(() -> new ObjectNotFoundException("Default role not found"));

        user.setRole(defaultRole);
        user.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        user.setFullName(userMapper.getFullName(user));

        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        user = userRepository.save(user);

        saveUserAuth(user, accessToken);

        return userMapper.toRegisteredUser(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetUser> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> users = usersPage.getContent();
        List<GetUser> dtos = users.isEmpty() ? List.of() : userMapper.toGetUsers(users);

        return new PageImpl<>(dtos, pageable, usersPage.getTotalElements());
    }

    private void validatePassword(CreateUserDto createUserDto) {
        if(!StringUtils.hasText(createUserDto.getPassword()) ||
                !StringUtils.hasText(createUserDto.getConfirmPassword())) {
            throw new InvalidPasswordException("Password and confirm password are required");
        }

        if(!createUserDto.getPassword().equals(createUserDto.getConfirmPassword())) {
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

    public void saveUserAuth(User user, String accessToken) {
        UserAuth userAuth = new UserAuth();
        userAuth.setToken(accessToken);
        userAuth.setUser(user);
        userAuth.setExpirationDate(jwtService.extractExpiration(accessToken));
        userAuth.setValid(true);

        userAuthRepository.save(userAuth);
    }
}
