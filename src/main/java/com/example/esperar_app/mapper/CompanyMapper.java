package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.inputs.company.CreateCompanyDto;
import com.example.esperar_app.persistence.entity.company.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mappings({
            @Mapping(target = "ceo", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Company createCompanyDtoToEntity(CreateCompanyDto createCompanyDto);

}
