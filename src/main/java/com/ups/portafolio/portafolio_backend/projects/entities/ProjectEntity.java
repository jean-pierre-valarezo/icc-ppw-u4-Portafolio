package com.ups.portafolio.portafolio_backend.projects.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String projectType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String participation;
    
    private String technologies; 
    
    private String repositoryUrl;
    
    private String demoUrl;
    
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity programmer;
}