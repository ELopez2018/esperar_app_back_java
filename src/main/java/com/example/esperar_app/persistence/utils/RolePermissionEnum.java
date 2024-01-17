package com.example.esperar_app.persistence.utils;

public enum RolePermissionEnum {
    // User's permissions
    SIGNUP,
    READ_ALL_USERS,
    READ_ONE_USER,
    CREATE_ONE_USER,
    UPDATE_ONE_USER,
    REMOVE_ONE_USER,

    // Auth permissions
    AUTHENTICATE,
    VALIDATE_TOKEN,
    READ_MY_PROFILE,
    LOGOUT,

    // Vehicle's permissions
    READ_ALL_VEHICLES,
    READ_ONE_VEHICLE,
    CREATE_ONE_VEHICLE,
    UPDATE_ONE_VEHICLE,
    REMOVE_ONE_VEHICLE,

    // Route's permissions
    READ_ALL_ROUTES,
    READ_ONE_ROUTE,
    CREATE_ONE_ROUTE,
    UPDATE_ONE_ROUTE,
    REMOVE_ONE_ROUTE,

    // Coordinate's permissions
    READ_ALL_COORDINATES,
    READ_ONE_COORDINATE,
    CREATE_ONE_COORDINATE,
    UPDATE_ONE_COORDINATE,
    REMOVE_ONE_COORDINATE,

    // Chats permissions
    CREATE_ONE_CHAT,
    REMOVE_ONE_CHAT
}

