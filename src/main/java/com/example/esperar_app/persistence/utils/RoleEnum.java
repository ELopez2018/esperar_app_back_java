package com.example.esperar_app.persistence.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum RoleEnum {
    ADMINISTRATOR(Arrays.asList(
            RolePermissionEnum.SIGNUP,
            RolePermissionEnum.READ_ALL_USERS,
            RolePermissionEnum.READ_ONE_USER,
            RolePermissionEnum.CREATE_ONE_USER,
            RolePermissionEnum.UPDATE_ONE_USER,
            RolePermissionEnum.REMOVE_ONE_USER,

            RolePermissionEnum.AUTHENTICATE,
            RolePermissionEnum.VALIDATE_TOKEN,
            RolePermissionEnum.READ_MY_PROFILE,
            RolePermissionEnum.LOGOUT,

            RolePermissionEnum.READ_ALL_VEHICLES,
            RolePermissionEnum.READ_ONE_VEHICLE,
            RolePermissionEnum.CREATE_ONE_VEHICLE,
            RolePermissionEnum.UPDATE_ONE_VEHICLE,
            RolePermissionEnum.REMOVE_ONE_VEHICLE,

            RolePermissionEnum.READ_ALL_ROUTES,
            RolePermissionEnum.READ_ONE_ROUTE,
            RolePermissionEnum.CREATE_ONE_ROUTE,
            RolePermissionEnum.UPDATE_ONE_ROUTE,
            RolePermissionEnum.REMOVE_ONE_ROUTE,

            RolePermissionEnum.READ_ALL_COORDINATES,
            RolePermissionEnum.READ_ONE_COORDINATE,
            RolePermissionEnum.CREATE_ONE_COORDINATE,
            RolePermissionEnum.UPDATE_ONE_COORDINATE,
            RolePermissionEnum.REMOVE_ONE_COORDINATE,

            RolePermissionEnum.GREETING,
            RolePermissionEnum.JS,
            RolePermissionEnum.WEBSOCKET,

            RolePermissionEnum.CREATE_ONE_NOTICE,
            RolePermissionEnum.READ_ALL_NOTICES,
            RolePermissionEnum.READ_ONE_NOTICE,
            RolePermissionEnum.UPDATE_ONE_NOTICE,
            RolePermissionEnum.REMOVE_ONE_NOTICE,

            RolePermissionEnum.CREATE_ONE_COMPANY,
            RolePermissionEnum.READ_ALL_COMPANIES,
            RolePermissionEnum.READ_ONE_COMPANY,
            RolePermissionEnum.UPDATE_ONE_COMPANY,
            RolePermissionEnum.REMOVE_ONE_COMPANY
    ));

    private final List<RolePermissionEnum> permissions;

    RoleEnum(List<RolePermissionEnum> permissions) {
        this.permissions = permissions;
    }

}
