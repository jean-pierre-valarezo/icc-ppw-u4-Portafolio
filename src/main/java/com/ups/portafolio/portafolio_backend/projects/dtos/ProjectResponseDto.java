package com.ups.portafolio.portafolio_backend.projects.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponseDto {
    private UUID id;
    private String title;
    private String description;
    private String imageUrl;
    private String programmerName; 
    private List<String> technologies;
}