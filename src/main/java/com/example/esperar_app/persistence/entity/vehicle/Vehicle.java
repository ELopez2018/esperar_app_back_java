package com.example.esperar_app.persistence.entity.vehicle;

import com.example.esperar_app.persistence.entity.security.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "license_plate")
    private String licensePlate;

    @Column(name = "secondary_plate")
    private String secondaryPlate;

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

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<User> drivers = new ArrayList<>();

    @Column(name = "soat_expiration_date")
    private Date soatExpirationDate;

    @Column(name = "tecno_mechanical_expiration_date")
    private Date tecnoMechanicalExpirationDate;

    public List<User> getDrivers() {
        System.out.println("Drivers: " + drivers.size());
        return drivers;
    }
    public void setDrivers(List<User> drivers) {
        this.drivers = drivers;
    }

    public void setSoatExpirationDate(String soatExpirationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            this.soatExpirationDate = sdf.parse(soatExpirationDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Fecha de expiración del SOAT inválida: "
                            + soatExpirationDate
                            + "El formato correcto es: mm-DD-yyyy");
        }
    }

    public void setTecnoMechanicalExpirationDate(String tecnoMechanicalExpirationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            this.tecnoMechanicalExpirationDate = sdf.parse(tecnoMechanicalExpirationDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Fecha de expiración de la tecnomecánica inválida: "
                            + tecnoMechanicalExpirationDate
                            + "El formato correcto es: mm-DD-yyyy");
        }
    }
}