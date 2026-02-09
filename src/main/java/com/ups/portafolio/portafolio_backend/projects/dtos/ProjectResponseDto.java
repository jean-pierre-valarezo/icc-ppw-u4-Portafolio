package com.ups.portafolio.portafolio_backend.projects.dtos;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponseDto {
    private UUID id;
    private String title;
    private String description;
    private String projectType;
    private String participation;
    private String technologies;
    private String repositoryUrl;
    private String demoUrl;
    private String imageUrl;
    private String programmerName;
}