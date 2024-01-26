package com.example.esperar_app.controller.company;

import com.example.esperar_app.persistence.dto.inputs.company.CreateCompanyDto;
import com.example.esperar_app.persistence.dto.inputs.company.UpdateCompanyDto;
import com.example.esperar_app.persistence.entity.company.Company;
import com.example.esperar_app.service.company.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<Company> create(@RequestBody @Valid CreateCompanyDto createCompanyDto) {
        Company company = companyService.create(createCompanyDto);
        if(company == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(company);
    }

    @GetMapping
    public ResponseEntity<Page<Company>> findAll(Pageable pageable) {
        Page<Company> companies = companyService.findAll(pageable);
        return ResponseEntity.ok(companies != null ? companies : Page.empty());
    }

    @GetMapping("{id}")
    public ResponseEntity<Company> findById(@PathVariable Long id) {
        Company company = companyService.findById(id);
        return ResponseEntity.of(Optional.ofNullable(company));
    }

    @PutMapping("{id}")
    public ResponseEntity<Company> update(@PathVariable Long id, @RequestBody @Valid UpdateCompanyDto updateCompanyDto) {
        Company company = companyService.update(id, updateCompanyDto);
        return ResponseEntity.of(Optional.ofNullable(company));
    }

    @DeleteMapping("{id}")
    public Void delete(@PathVariable Long id) {
        companyService.delete(id);
        return null;
    }

}
