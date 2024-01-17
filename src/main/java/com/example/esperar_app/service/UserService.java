package com.example.esperar_app.service;

import com.example.esperar_app.persistence.dto.inputs.CreateUserDto;
import com.example.esperar_app.persistence.dto.inputs.RegisteredUser;
import com.example.esperar_app.persistence.dto.inputs.UpdateUserDto;
import com.example.esperar_app.persistence.dto.responses.GetUser;
import com.example.esperar_app.persistence.entity.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    RegisteredUser create(CreateUserDto createUserDto);

    Optional<User> findOneByUsername(String username);

    Page<GetUser> findAll(Pageable pageable);

    GetUser findById(Long id);

    GetUser update(Long id, UpdateUserDto updateUserDto);

    void delete(Long id);
}
