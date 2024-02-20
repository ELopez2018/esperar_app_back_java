package com.example.esperar_app.persistence.entity.security;

import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.utils.DocumentType;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import com.example.esperar_app.persistence.utils.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "users")
@Table(name = "users")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class User implements UserDetails {
    /**
     * Unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email
     */
    @Column(unique = true)
    private String email;

    /**
     * Username
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Password
     */
    @JsonIgnore
    @Column()
    private String password;

    /**
     * Associated role to user
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * Full name
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * User profile image
     */
    @Column(name = "image")
    private String image;

    /**
     * User first name
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * User second name
     */
    @Column(name = "second_name")
    private String secondName;

    /**
     * User last name
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Birthdate date
     */
    @Column(name = "birthdate")
    private String birthdate;

    /**
     * Gender
     */
    @Column(name = "gender")
    private String gender;

    /**
     * Document number
     */
    @Column(name = "document_number", unique = true)
    private Long documentNumber;

    /**
     * Document type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_status")
    private UserChatStatus chatStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    /**
     * Phone number
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Created at date
     */
    @Column(name = "created_at")
    public Date createdAt;

    /**
     * Updated at date
     */
    @Column(name = "updated_at")
    public String updatedAt;

    /**
     * Deleted at date
     */
    @Column(name = "deleted_at")
    public String deletedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private List<UserAuth> userAuthList;

    @Column
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

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "accepted_terms_at", nullable = false)
    private Date acceptedTermsAt;

    @Column(name = "confirmed_account_at")
    private Date confirmedAccountAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) return null;

        if (role.getPermissions() == null) return null;

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
}