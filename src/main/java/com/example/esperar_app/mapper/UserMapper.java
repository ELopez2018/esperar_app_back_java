package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.user.CreateUserDto;
import com.example.esperar_app.persistence.dto.user.CurrentUserDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.user.RegisteredUser;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.entity.security.User;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.StringJoiner;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mappings({
            @Mapping(source = "documentNumber", target = "identificationData.documentNumber"),
            @Mapping(source = "documentType", target = "identificationData.documentType"),
            @Mapping(target = "currentVehicle", ignore = true),
            @Mapping(target = "currentCompany", ignore = true),
    })
    CurrentUserDto toCurrentUserDto(User user);

    @Mappings({
            @Mapping(source = "confirmPassword", target = "password"),
            @Mapping(source = "username", target = "username"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "currentCountry", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "fullName", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "image", ignore = true),
            @Mapping(target = "role", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "userAuthList", ignore = true),
            @Mapping(target = "companies", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "vehicle", ignore = true),
            @Mapping(target = "chatStatus", ignore = true)
    })
    User createUserDtoToUser(CreateUserDto createUserDto);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(target = "accessToken", ignore = true),
            @Mapping(target = "role", expression = "java(mapRole(user.getRole()))"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "documentNumber", target = "documentNumber"),
            @Mapping(source = "documentType", target = "documentType"),
            @Mapping(source = "gender", target = "gender"),
            @Mapping(source = "birthdate", target = "birthdate"),
    })
    RegisteredUser toRegisteredUser(User user);

    default String mapRole(Role role) {
        return role != null ? role.getName() : null;
    }

    default String getFullName(User user) {
        if (user == null) {
            return null;
        }

        StringJoiner fullName = new StringJoiner(" ");
        appendIfNotNull(fullName, user.getFirstName());
        appendIfNotNull(fullName, user.getSecondName());
        appendIfNotNull(fullName, user.getLastName());

        return fullName.toString();
    }

    default void appendIfNotNull(StringJoiner joiner, String value) {
        if (value != null) {
            joiner.add(value);
        }
    }

    @Mappings({
            @Mapping(source = "documentNumber", target = "identificationData.documentNumber"),
            @Mapping(source = "documentType", target = "identificationData.documentType"),
            @Mapping(target = "currentVehicle", ignore = true),
            @Mapping(target = "currentCompany", ignore = true),
    })
    GetUserDto toGetUserDto(User user);

    @InheritConfiguration
    List<GetUserDto> toGetUserDtos(List<User> connectedUsers);
}