package com.example.esperar_app.persistence.dto.responses.auth;

import com.example.esperar_app.persistence.dto.responses.GetCompanyDto;
import com.example.esperar_app.persistence.entity.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CurrentUserDto {
    private Long id;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private Role role;
    private GetCompanyDto company;
}
