package com.ups.portafolio.portafolio_backend.projects.repository;

import com.ups.portafolio.portafolio_backend.projects.entities.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {
    
    Page<ProjectEntity> findByProgrammerId(UUID programmerId, Pageable pageable);
}