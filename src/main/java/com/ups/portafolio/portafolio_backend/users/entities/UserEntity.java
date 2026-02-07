package com.ups.portafolio.portafolio_backend.users.entities;

import com.ups.portafolio.portafolio_backend.projects.entities.ProjectEntity; // Importa Proyectos
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore 
    private String password;

    private String role; 

    @OneToMany(mappedBy = "programmer", cascade = CascadeType.ALL)
    private List<ProjectEntity> projects;
}
