package com.example.esperar_app.service.auth.impl;

import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.repository.security.RoleRepository;
import com.example.esperar_app.service.auth.RoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Value("${security.default.role}")
    private String defaultRole;

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findDefaultRole() {
        return roleRepository.findByName(defaultRole);
    }
}
