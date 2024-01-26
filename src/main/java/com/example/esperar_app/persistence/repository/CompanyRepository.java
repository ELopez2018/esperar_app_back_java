package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> { }
