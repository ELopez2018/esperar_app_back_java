package com.example.esperar_app.service.company;

import com.example.esperar_app.persistence.dto.company.GetCompanyDto;
import com.example.esperar_app.persistence.dto.company.UpdateCompanyDto;
import com.example.esperar_app.persistence.dto.inputs.company.CreateCompanyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    GetCompanyDto create(CreateCompanyDto createCompanyDto);

    Page<GetCompanyDto> findAll(Pageable pageable);

    GetCompanyDto findById(Long id);

    GetCompanyDto update(Long id, UpdateCompanyDto updateCompanyDto);

    void delete(Long id);
}
