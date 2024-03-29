package com.example.esperar_app.service.auth;

import com.example.esperar_app.persistence.entity.security.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();
    Role getDriverRole();
}
