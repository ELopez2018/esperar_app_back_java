package com.example.esperar_app.service.impl;

import com.example.esperar_app.dto.inputs.CreateUserDto;
import com.example.esperar_app.exception.InvalidPasswordException;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.service.RoleService;
import com.example.esperar_app.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User create(CreateUserDto createUserDto) {
        validatePassword(createUserDto);

        // TODO: Añadir demás campos
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setUsername(createUserDto.getUsername());

        Role defaultRole = roleService.findDefaultRole()
                        .orElseThrow(() -> new ObjectNotFoundException("Default role not found"));

        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
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
}
