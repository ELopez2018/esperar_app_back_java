package com.example.esperar_app.service;

import com.example.esperar_app.dto.inputs.CreateUserDto;
import com.example.esperar_app.persistence.entity.security.User;

import java.util.Optional;

public interface UserService {
    User create(CreateUserDto createUserDto);

    Optional<User> findOneByUsername(String username);
}
