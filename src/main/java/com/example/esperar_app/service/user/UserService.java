package com.example.esperar_app.service.user;

import com.example.esperar_app.persistence.dto.inputs.user.CreateUserDto;
import com.example.esperar_app.persistence.dto.inputs.user.RegisteredUser;
import com.example.esperar_app.persistence.dto.inputs.user.UpdateUserDto;
import com.example.esperar_app.persistence.dto.responses.GetUser;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    RegisteredUser create(CreateUserDto createUserDto);

    Optional<User> findOneByUsername(String username);

    Page<GetUser> findAll(Pageable pageable);

    GetUser findById(Long id);

    GetUser update(Long id, UpdateUserDto updateUserDto);

    void delete(Long id);

    List<User> findVehicleDrivers(Long id);

    void disconnectUser(Long id);

    List<User> findConnectedUsers();

    User connectUser(User user);
}
