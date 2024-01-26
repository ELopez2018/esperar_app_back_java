package com.example.esperar_app.service.company;

import com.example.esperar_app.persistence.dto.inputs.company.CreateCompanyDto;
import com.example.esperar_app.persistence.dto.inputs.company.UpdateCompanyDto;
import com.example.esperar_app.persistence.entity.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Company create(CreateCompanyDto createCompanyDto);

    Page<Company> findAll(Pageable pageable);

    Company findById(Long id);

    Company update(Long id, UpdateCompanyDto updateCompanyDto);

    void delete(Long id);
}
