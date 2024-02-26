package com.example.esperar_app.service.auth.impl;

import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.repository.security.RoleRepository;
import com.example.esperar_app.service.auth.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Value("${security.default.role}")
    private String defaultRole;

    private final RoleRepository roleRepository;

    private static final Logger logger = LogManager.getLogger();

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findDefaultRole() {
        logger.info("Finding default role");
        return roleRepository.findByName(defaultRole);
    }
}
