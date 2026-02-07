package com.ups.portafolio.portafolio_backend.projects.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ups.portafolio.portafolio_backend.projects.dtos.CreateProjectDto;
import com.ups.portafolio.portafolio_backend.projects.dtos.ProjectResponseDto;
import com.ups.portafolio.portafolio_backend.projects.services.ProjectService;
import com.ups.portafolio.portafolio_backend.users.services.UserDetailsImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> create(
            @RequestBody CreateProjectDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(projectService.create(dto, currentUser));
    }

    @GetMapping("/my-projects")
    public ResponseEntity<Page<ProjectResponseDto>> getMyProjects(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tech,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(projectService.getMyProjects(title, tech, page, size, currentUser));
    }

    @GetMapping("/public/{userId}")
    public ResponseEntity<Page<ProjectResponseDto>> getPublicProjects(
            @PathVariable UUID userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tech,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getPublicProjects(userId, title, tech, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        projectService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
