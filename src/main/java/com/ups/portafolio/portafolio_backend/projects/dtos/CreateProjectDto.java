package com.ups.portafolio.portafolio_backend.projects.dtos;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProjectDto {
    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    private String imageUrl;

    private List<String> technologies; 
}