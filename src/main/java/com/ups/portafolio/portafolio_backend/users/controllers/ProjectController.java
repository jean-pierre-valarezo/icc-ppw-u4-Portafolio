package com.ups.portafolio.portafolio_backend.users.controllers;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.ups.portafolio.portafolio_backend.users.entities.ProjectEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.services.ProjectService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
     private final ProjectService projectService;

    @PostMapping
    public ProjectEntity createProject(@RequestBody ProjectEntity project) {
        return projectService.createProject(project);
    }

    @GetMapping
    public List<ProjectEntity> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectEntity getProjectById(@PathVariable UUID id) {
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }

    @PostMapping("/programmer")
    public List<ProjectEntity> getProjectsByProgrammer(@RequestBody UserEntity programmer) {
        return projectService.getProjectsByProgrammer(programmer);
    }
}
