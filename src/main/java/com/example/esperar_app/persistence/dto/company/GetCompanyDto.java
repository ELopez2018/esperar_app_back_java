package com.example.esperar_app.persistence.dto.company;

import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object for getting a company
 */
@Getter @Setter @AllArgsConstructor
public class GetCompanyDto {
    /**
     * The company identifier
     */
    private Long id;

    /**
     * The company name
     */
    private String name;

    /**
     * The company address
     */
    private String address;

    /**
     * The company phone number
     */
    private String phoneNumber;

    /**
     * The company email
     */
    private String email;

    /**
     * The company website
     */
    private String website;

    /**
     * The company ceo identifier
     */
    private Long ceoId;

    /**
     * TODO: Change to GetMemberDto
     * The company members identifiers
     */
    private List<Long> membersIds;

    /**
     * The company vehicles
     */
    private List<GetVehicleDto> vehicles;
}
