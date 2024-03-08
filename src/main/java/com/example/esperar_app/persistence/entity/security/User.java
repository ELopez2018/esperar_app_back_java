package com.example.esperar_app.persistence.entity.security;

import com.example.esperar_app.exception.ExpirationDateException;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.utils.DocumentType;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import com.example.esperar_app.persistence.utils.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "users")
@Table(name = "users")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column()
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "image")
    private String image;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthdate")
    private String birthdate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "document_number", unique = true)
    private Long documentNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_status")
    private UserChatStatus chatStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    public Date createdAt;

    @Column(name = "updated_at")
    public String updatedAt;

    @Column(name = "deleted_at")
    public String deletedAt;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> ownedVehicles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<UserAuth> userAuthList;

    @Column(unique = true)
    private String nit;

    @Column
    private String address;

    @Column
    private String neighborhood;

    @Column
    private String city;

    @Column
    private String department;

    @Column
    private String country;

    @Column
    private String cellPhone;

    @Column
    private String whatsapp;

    @Column(name = "accepted_terms_at", nullable = false)
    private Date acceptedTermsAt;

    @Column(name = "confirmed_account_at")
    private Date confirmedAccountAt;

    @Column(name = "change_password_token")
    private String changePasswordToken;

    // CHAMBER OF COMMERCE
    @Column(name = "chamber_of_commerce_url")
    private String chamberOfCommerceUrl;

    @Column(name = "chamber_of_commerce_created_at")
    private Date chamberOfCommerceCreatedAt;

    @Column(name = "chamber_of_commerce_updated_at")
    private Date chamberOfCommerceUpdatedAt;

    // DRIVER LICENSE
    @Column(name = "driver_license_url")
    private String driverLicenseUrl;

    @Column(name = "driver_license_created_at")
    private Date driverLicenseCreatedAt;

    @Column(name = "driver_license_updated_at")
    private Date driverLicenseUpdatedAt;

    @Column(name = "license_expiration_date")
    private Date licenseExpirationDate;

    // PROFILE IMAGE
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private User company;

    @OneToMany(mappedBy = "company")
    private List<User> drivers;

    @OneToOne(mappedBy = "mainDriver")
    private Vehicle mainVehicle;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) return null;

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setLicenseExpirationDate(String licenseExpirationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            this.licenseExpirationDate = sdf.parse(licenseExpirationDate);
        } catch (Exception e) {
            throw new ExpirationDateException(
                    "Fecha de expiración de la licencia de conducción inválida: "
                            + licenseExpirationDate
                            + "El formato correcto es: MM-dd-yyyy"
            );
        }
    }
}