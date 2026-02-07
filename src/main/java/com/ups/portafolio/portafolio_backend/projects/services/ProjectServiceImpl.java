package com.ups.portafolio.portafolio_backend.projects.services;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
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
    private final TechnologyRepository technologyRepository;

    @Override
    @Transactional
    public ProjectResponseDto create(CreateProjectDto dto, UserDetailsImpl currentUser) {
        UserEntity programmer = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

       
        Set<TechnologyEntity> techEntities = new HashSet<>();
        if (dto.getTechnologies() != null) {
            for (String techName : dto.getTechnologies()) {
                TechnologyEntity tech = technologyRepository.findByName(techName)
                        .orElseGet(() -> technologyRepository.save(
                                TechnologyEntity.builder().name(techName).build()
                        ));
                techEntities.add(tech);
            }
        }

        ProjectEntity project = ProjectEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .programmer(programmer)
                .technologies(techEntities)
                .build();

        return mapToDto(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getMyProjects(String title, String tech, int page, int size, UserDetailsImpl currentUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        return projectRepository.search(currentUser.getId(), title, tech, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getPublicProjects(UUID userId, String title, String tech, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return projectRepository.search(userId, title, tech, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public void delete(UUID id, UserDetailsImpl currentUser) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        validateOwnership(project, currentUser);

        projectRepository.delete(project);
    }

    private void validateOwnership(ProjectEntity project, UserDetailsImpl currentUser) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !project.getProgrammer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No puedes eliminar este proyecto porque no es tuyo.");
        }
    }

    private ProjectResponseDto mapToDto(ProjectEntity entity) {
        return ProjectResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .programmerName(entity.getProgrammer().getName())
                .technologies(entity.getTechnologies().stream()
                        .map(TechnologyEntity::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}