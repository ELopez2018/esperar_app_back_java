package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.inputs.company.CreateCompanyDto;
import com.example.esperar_app.persistence.dto.responses.CompanyResponse;
import com.example.esperar_app.persistence.entity.company.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mappings({
            @Mapping(target = "ceo", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "members", ignore = true),
            @Mapping(target = "vehicles", ignore = true)
    })
    Company createCompanyDtoToEntity(CreateCompanyDto createCompanyDto);

    @Mappings({
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "website", target = "website"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "name", target = "name"),
            @Mapping(target = "ceoId", ignore = true),
            @Mapping(target = "vehiclesIds", ignore = true)
    })
    CompanyResponse companyToCompanyResponse(Company company);
}
