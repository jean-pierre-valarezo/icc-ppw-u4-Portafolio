package com.ups.portafolio.portafolio_backend.projects.dtos;

import lombok.Data;

@Data
public class CreateProjectDto {
    private String title;
    private String description;
    private String projectType;   
    private String participation;  
    private String technologies;   
    private String repositoryUrl;  
    private String demoUrl;       
    private String imageUrl;
}