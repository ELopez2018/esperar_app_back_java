package com.example.esperar_app.service.auth.impl;

import com.example.esperar_app.persistence.dto.inputs.SavePermission;
import com.example.esperar_app.persistence.dto.responses.ShowPermission;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.entity.security.Operation;
import com.example.esperar_app.persistence.entity.security.Permission;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.repository.security.OperationRepository;
import com.example.esperar_app.persistence.repository.security.PermissionRepository;
import com.example.esperar_app.persistence.repository.security.RoleRepository;
import com.example.esperar_app.service.auth.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    private final OperationRepository operationRepository;

    @Autowired
    public PermissionServiceImpl(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            OperationRepository operationRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public Page<ShowPermission> findAll(Pageable pageable) {
        return permissionRepository.findAll(pageable)
                .map(this::mapEntityToShowDto);
    }

    private ShowPermission mapEntityToShowDto(Permission grantedPermission) {
        if(grantedPermission == null) return null;

        ShowPermission showDto = new ShowPermission();
        showDto.setId(grantedPermission.getId());
        showDto.setRole(grantedPermission.getRole().getName());
        showDto.setOperation(grantedPermission.getOperation().getName());
        showDto.setHttpMethod(grantedPermission.getOperation().getHttpMethod());
        showDto.setModule(grantedPermission.getOperation().getModule().getName());

        return showDto;
    }

    @Override
    public Optional<ShowPermission> findOneById(Long permissionId) {
        return permissionRepository.findById(permissionId)
                .map(this::mapEntityToShowDto);
    }

    @Override
    public ShowPermission createOne(SavePermission savePermission) {

        Permission newPermission = new Permission();

        Operation operation = operationRepository.findByName(savePermission.getOperation())
                .orElseThrow(() -> new ObjectNotFoundException("Operation not found. Operation: " + savePermission.getOperation()));
        newPermission.setOperation(operation);

        Role role = roleRepository.findByName(savePermission.getRole()).orElseThrow(
                () -> new ObjectNotFoundException("Role not found. Role: " + savePermission.getRole()));
        newPermission.setRole(role);

        permissionRepository.save(newPermission);
        return this.mapEntityToShowDto(newPermission);
    }

    @Override
    public ShowPermission deleteOneById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ObjectNotFoundException("Permission not found. Permission: " + permissionId));

        permissionRepository.delete(permission);

        return this.mapEntityToShowDto(permission);
    }
}

