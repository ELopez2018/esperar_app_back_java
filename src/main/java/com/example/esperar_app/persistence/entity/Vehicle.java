package com.example.esperar_app.persistence.entity;

import com.example.esperar_app.persistence.entity.company.Company;
import com.example.esperar_app.persistence.entity.security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vehicles")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "license_plate")
    private String licensePlate;

    @Column
    private String model;

    @Column
    private String brand;

    @Column
    private Integer year;

    @Column
    private String color;

    @Column(name = "cylinder_capacity")
    private Double cylinderCapacity;

    @Column
    private Integer capacity;

    @Column
    private Integer occupancy;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private User driver;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id")
    private Company company;
}