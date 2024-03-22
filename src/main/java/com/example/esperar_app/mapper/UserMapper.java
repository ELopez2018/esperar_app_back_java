package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.user.CreateLegalPersonDto;
import com.example.esperar_app.persistence.dto.user.CreateNaturalPersonDto;
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
    })
    CurrentUserDto toCurrentUserDto(User user);

    @Mappings({
            @Mapping(source = "confirmPassword", target = "password"),
            @Mapping(source = "username", target = "username"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "licenseExpirationDate", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "fullName", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "image", ignore = true),
            @Mapping(target = "role", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "userAuthList", ignore = true),
            @Mapping(target = "vehicle", ignore = true),
            @Mapping(target = "chatStatus", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "acceptedTermsAt", ignore = true),
            @Mapping(target = "cellPhone", ignore = true),
            @Mapping(target = "city", ignore = true),
            @Mapping(target = "confirmedAccountAt", ignore = true),
            @Mapping(target = "country", ignore = true),
            @Mapping(target = "department", ignore = true),
            @Mapping(target = "neighborhood", ignore = true),
            @Mapping(target = "nit", ignore = true),
            @Mapping(target = "profileImageUrl", ignore = true),
            @Mapping(target = "userType", ignore = true),
            @Mapping(target = "whatsapp", ignore = true),
            @Mapping(target = "changePasswordToken", ignore = true)
    })
    User createUserDtoToUser(CreateNaturalPersonDto createNaturalPersonDto);

    @Mappings({
            @Mapping(target = "currentVehicle", ignore = true),
            @Mapping(target = "accessToken", ignore = true),
            @Mapping(source = "documentType", target = "identificationData.documentType"),
            @Mapping(source = "documentNumber", target = "identificationData.documentNumber"),
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
    })
    GetUserDto toGetUserDto(User user);

    @InheritConfiguration
    List<GetUserDto> toGetUserDtos(List<User> connectedUsers);

    @Mappings({
            @Mapping(target = "birthdate", ignore = true),
            @Mapping(target = "chatStatus", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "documentNumber", ignore = true),
            @Mapping(target = "documentType", ignore = true),
            @Mapping(target = "firstName", ignore = true),
            @Mapping(target = "fullName", source = "name"),
            @Mapping(target = "gender", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "image", ignore = true),
            @Mapping(target = "lastName", ignore = true),
            @Mapping(target = "role", ignore = true),
            @Mapping(target = "secondName", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "userAuthList", ignore = true),
            @Mapping(target = "vehicle", ignore = true),
            @Mapping(target = "profileImageUrl", ignore = true),
            @Mapping(target = "acceptedTermsAt", ignore = true),
            @Mapping(target = "changePasswordToken", ignore = true),
            @Mapping(target = "confirmedAccountAt", ignore = true),
            @Mapping(target = "licenseExpirationDate", ignore = true),
            @Mapping(target = "userType", ignore = true)
    })
    User createLegalPersonDtoToUser(CreateLegalPersonDto createLegalPersonDto);
}