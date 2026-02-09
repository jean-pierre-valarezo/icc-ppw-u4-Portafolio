package com.ups.portafolio.portafolio_backend.projects.services;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import com.ups.portafolio.portafolio_backend.projects.dtos.CreateProjectDto;
import com.ups.portafolio.portafolio_backend.projects.dtos.ProjectResponseDto;

import java.util.UUID;

public interface ProjectService {
    
    ProjectResponseDto create(CreateProjectDto dto, UserDetails currentUser);

    Page<ProjectResponseDto> getMyProjects(String title, int page, int size, UserDetails currentUser);

    void delete(UUID id, UserDetails currentUser);

    Page<ProjectResponseDto> getPublicProjects(UUID userId, String title, int page, int size);
}