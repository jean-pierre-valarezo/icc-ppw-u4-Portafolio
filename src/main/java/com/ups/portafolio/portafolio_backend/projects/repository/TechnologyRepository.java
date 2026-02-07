package com.ups.portafolio.portafolio_backend.projects.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ups.portafolio.portafolio_backend.projects.entities.TechnologyEntity;

@Repository
public interface TechnologyRepository extends JpaRepository<TechnologyEntity, UUID> {
    Optional<TechnologyEntity> findByName(String name);
}
