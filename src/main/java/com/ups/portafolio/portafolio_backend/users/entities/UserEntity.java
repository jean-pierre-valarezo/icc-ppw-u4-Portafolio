package com.ups.portafolio.portafolio_backend.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ups.portafolio.portafolio_backend.projects.entities.ProjectEntity;
import com.ups.portafolio.portafolio_backend.schedules.entity.ScheduleEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails{

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; 

    @JsonIgnore
    @OneToMany(mappedBy = "programmer", cascade = CascadeType.ALL)
    private List<ProjectEntity> projects;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ScheduleEntity> schedules;

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
     }

     @Override
     public String getUsername() {
        return email;
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
        return active; 
    }
}
