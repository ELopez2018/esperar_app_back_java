package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.company.CompanyResponse;
import com.example.esperar_app.persistence.dto.company.GetCompanyDto;
import com.example.esperar_app.persistence.dto.inputs.company.CreateCompanyDto;
import com.example.esperar_app.persistence.entity.company.Company;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

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
            @Mapping(target = "ceoId", source = "ceo.id"),
            @Mapping(target = "membersIds", ignore = true),
    })
    GetCompanyDto companyToGetCompanyDto(Company company);

    @InheritConfiguration
    List<GetCompanyDto> companiesToGetCompanyDto(List<Company> companies);
}
