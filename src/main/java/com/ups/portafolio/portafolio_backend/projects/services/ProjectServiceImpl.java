package com.ups.portafolio.portafolio_backend.projects.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ups.portafolio.portafolio_backend.projects.dtos.CreateProjectDto;
import com.ups.portafolio.portafolio_backend.projects.dtos.ProjectResponseDto;
import com.ups.portafolio.portafolio_backend.projects.entities.ProjectEntity;
import com.ups.portafolio.portafolio_backend.projects.entities.TechnologyEntity;
import com.ups.portafolio.portafolio_backend.projects.repository.ProjectRepository;
import com.ups.portafolio.portafolio_backend.projects.repository.TechnologyRepository;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;
import com.ups.portafolio.portafolio_backend.users.services.UserDetailsImpl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository; 

    @Override
    @Transactional
    public ProjectResponseDto create(CreateProjectDto dto, UserDetails currentUser) {
        UserEntity programmer = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + currentUser.getUsername()));

        ProjectEntity project = ProjectEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .projectType(dto.getProjectType())       
                .participation(dto.getParticipation())   
                .technologies(dto.getTechnologies())     
                .repositoryUrl(dto.getRepositoryUrl())  
                .demoUrl(dto.getDemoUrl())               
                .imageUrl(dto.getImageUrl())
                .programmer(programmer)
                .build();

        return mapToDto(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getMyProjects(String title, int page, int size, UserDetails currentUser) {
        UserEntity user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        
        return projectRepository.findByProgrammerId(user.getId(), pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getPublicProjects(UUID userId, String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        
        return projectRepository.findByProgrammerId(userId, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public void delete(UUID id, UserDetails currentUser) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        validateOwnership(project, currentUser);
        projectRepository.delete(project);
    }

    private void validateOwnership(ProjectEntity project, UserDetails currentUser) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")); 

        if (!isAdmin && !project.getProgrammer().getEmail().equals(currentUser.getUsername())) {
            throw new AccessDeniedException("No puedes eliminar este proyecto porque no es tuyo.");
        }
    }

    private ProjectResponseDto mapToDto(ProjectEntity entity) {
        return ProjectResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .projectType(entity.getProjectType())
                .participation(entity.getParticipation())
                .technologies(entity.getTechnologies()) 
                .repositoryUrl(entity.getRepositoryUrl())
                .demoUrl(entity.getDemoUrl())
                .imageUrl(entity.getImageUrl())
                .programmerName(entity.getProgrammer().getName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getAllPublicProjects() {
        List<ProjectEntity> projects = projectRepository.findAll();
    
        return projects.stream()
            .map(this::mapToDto) 
            .collect(Collectors.toList());
    }
}