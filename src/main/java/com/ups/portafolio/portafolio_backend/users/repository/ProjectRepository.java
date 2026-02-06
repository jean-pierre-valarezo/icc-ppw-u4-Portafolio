package com.ups.portafolio.portafolio_backend.users.repository;


import com.ups.portafolio.portafolio_backend.users.entities.ProjectEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {
    
    List<ProjectEntity> findByProgrammer(UserEntity programmer);
}
