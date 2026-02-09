package com.ups.portafolio.portafolio_backend.projects.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.ups.portafolio.portafolio_backend.projects.dtos.CreateProjectDto;
import com.ups.portafolio.portafolio_backend.projects.dtos.ProjectResponseDto;
import com.ups.portafolio.portafolio_backend.projects.services.ProjectService;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> create(
            @RequestBody CreateProjectDto dto,
            @AuthenticationPrincipal UserDetails currentUser 
    ) {
        return ResponseEntity.ok(projectService.create(dto, currentUser));
    }

    @GetMapping("/my-projects")
    public ResponseEntity<Page<ProjectResponseDto>> getMyProjects(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        return ResponseEntity.ok(projectService.getMyProjects(title, page, size, currentUser));
    }

    @GetMapping("/public/{userId}")
    public ResponseEntity<Page<ProjectResponseDto>> getPublicProjects(
            @PathVariable UUID userId,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getPublicProjects(userId, title, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        projectService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}