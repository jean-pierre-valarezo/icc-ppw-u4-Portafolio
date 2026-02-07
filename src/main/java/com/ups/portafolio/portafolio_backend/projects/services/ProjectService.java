package com.ups.portafolio.portafolio_backend.projects.services;

import org.springframework.data.domain.Page;
import com.ups.portafolio.portafolio_backend.projects.dtos.CreateProjectDto;
import com.ups.portafolio.portafolio_backend.projects.dtos.ProjectResponseDto;
import com.ups.portafolio.portafolio_backend.users.services.UserDetailsImpl;

import java.util.UUID;

public interface ProjectService {
    
    ProjectResponseDto create(CreateProjectDto dto, UserDetailsImpl currentUser);

    Page<ProjectResponseDto> getMyProjects(String title, String tech, int page, int size, UserDetailsImpl currentUser);

    void delete(UUID id, UserDetailsImpl currentUser);

    Page<ProjectResponseDto> getPublicProjects(UUID userId, String title, String tech, int page, int size);
}