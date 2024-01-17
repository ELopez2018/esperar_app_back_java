package com.example.esperar_app.persistence.utils;

import java.util.Arrays;
import java.util.List;

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

            RolePermissionEnum.CREATE_ONE_CHAT,
            RolePermissionEnum.REMOVE_ONE_CHAT
    ));

    private List<RolePermissionEnum> permissions;

    RoleEnum(List<RolePermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public List<RolePermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermissionEnum> permissions) {
        this.permissions = permissions;
    }
}
