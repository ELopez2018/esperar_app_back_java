package com.example.esperar_app.service.auth;

import com.example.esperar_app.persistence.dto.inputs.SavePermission;
import com.example.esperar_app.persistence.dto.responses.ShowPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface PermissionService {
    Page<ShowPermission> findAll(Pageable pageable);

    Optional<ShowPermission> findOneById(Long permissionId);

    ShowPermission createOne(SavePermission savePermission);

    ShowPermission deleteOneById(Long permissionId);
}
