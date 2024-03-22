package com.example.esperar_app.persistence.dto.user;

import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.utils.Gender;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RegisteredUser implements Serializable {
    private String accessToken;

    /**
     * The user id
     */
    private Long id;

    /**
     * The user email
     */
    private String email;

    /**
     * The user username
     */
    private String username;

    /**
     * The user role
     */
    private Role role;

    /**
     * The user full name
     */
    private String fullName;

    /**
     * The user image
     */
    private String image;

    /**
     * The user first name
     */
    private String firstName;

    /**
     * The user second name
     */
    private String secondName;

    /**
     * The user last name
     */
    private String lastName;

    /**
     * The user birthdate
     */
    private String birthdate;

    /**
     * User's gender
     */
    private Gender gender;

    /**
     * The user's identification data
     */
    private IdentificationDocumentDto identificationData;

    /**
     * User's websocket status
     */
    private UserChatStatus chatStatus;

    /**
     * The user's phone
     */
    private String phone;

    /**
     * When the user was created
     */
    private Date createdAt;

    /**
     * When the user was updated
     */
    private String updatedAt;

    /**
     * When the user was deleted
     */
    private String deletedAt;

    /**
     * The user's current vehicle information
     */
    private GetVehicleDto currentVehicle;
}