package com.example.esperar_app.service.company;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.CompanyMapper;
import com.example.esperar_app.persistence.dto.inputs.company.CreateCompanyDto;
import com.example.esperar_app.persistence.dto.inputs.company.UpdateCompanyDto;
import com.example.esperar_app.persistence.dto.responses.CompanyResponse;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.entity.company.Company;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.repository.CompanyRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserRepository userRepository;

    @Autowired
    public CompanyServiceImpl(
            CompanyRepository companyRepository,
            CompanyMapper companyMapper,
            UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.userRepository = userRepository;
    }

    /**
     * Create a company
     * @param createCompanyDto - company data to create
     * @return created company
     */
    @Override
    public Company create(CreateCompanyDto createCompanyDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User ceo = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("Ceo not found"));

        Company company = companyMapper.createCompanyDtoToEntity(createCompanyDto);
        company.setCeo(ceo);

        Company newCompany = companyRepository.save(company);

        ceo.setCompany(newCompany);
        ceo.getCompanies().add(newCompany);
        userRepository.save(ceo);

        return newCompany;
    }

    /**
     * Get all companies
     * @param pageable - pagination
     * @return page of companies
     */
    @Override
    public Page<Company> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    /**
     * Find company by id
     * @param id - company id
     * @return company
     */
    @Override
    public CompanyResponse findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found"));

        CompanyResponse companyResponse = companyMapper.companyToCompanyResponse(company);
        companyResponse.setCeoId(company.getCeo().getId());

        if(company.getVehicles() != null) {
            for (Vehicle vehicle : company.getVehicles()) {
                companyResponse.setVehiclesIds(Collections.singletonList(vehicle.getId()));
            }
        }

        return companyResponse;
    }

    /**
     * Update company
     * @param id - company id
     * @param updateCompanyDto - company data to update
     * @return updated company
     */
    @Override
    public Company update(Long id, UpdateCompanyDto updateCompanyDto) {
        Company existingCompany = companyRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found"));

        BeanUtils.copyProperties(updateCompanyDto, existingCompany, getStrings(updateCompanyDto));

        return companyRepository.save(existingCompany);
    }

    /**
     * Delete company
     * @param id - company id
     */
    @Override
    public void delete(Long id) {
        Company company = companyRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found"));

        companyRepository.delete(company);
    }

}
