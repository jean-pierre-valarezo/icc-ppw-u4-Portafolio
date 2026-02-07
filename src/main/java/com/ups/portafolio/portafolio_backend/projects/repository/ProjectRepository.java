package com.ups.portafolio.portafolio_backend.projects.repository;


import com.ups.portafolio.portafolio_backend.projects.entities.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {

    @Query("SELECT DISTINCT p FROM ProjectEntity p " +
           "LEFT JOIN p.technologies t " +
           "WHERE (:userId IS NULL OR p.programmer.id = :userId) " +
           "AND (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:tech IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :tech, '%')))")
    Page<ProjectEntity> search(
            @Param("userId") UUID userId,
            @Param("title") String title,
            @Param("tech") String tech,
            Pageable pageable
    );
}