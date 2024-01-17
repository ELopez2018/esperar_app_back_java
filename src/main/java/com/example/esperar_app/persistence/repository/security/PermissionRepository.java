package com.example.esperar_app.persistence.repository.security;

import com.example.esperar_app.persistence.entity.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> { }
