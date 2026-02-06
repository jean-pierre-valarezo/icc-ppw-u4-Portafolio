package com.ups.portafolio.portafolio_backend.users.services;




import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ups.portafolio.portafolio_backend.users.entities.ProjectEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.ProjectRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectEntity createProject(ProjectEntity project) {
        return projectRepository.save(project);
    }

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<ProjectEntity> getProjectsByProgrammer(UserEntity programmer) {
        return projectRepository.findByProgrammer(programmer);
    }

    public ProjectEntity getProjectById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }
}
