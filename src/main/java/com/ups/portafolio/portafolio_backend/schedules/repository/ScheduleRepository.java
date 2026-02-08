package com.ups.portafolio.portafolio_backend.schedules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ups.portafolio.portafolio_backend.schedules.entity.ScheduleEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, UUID> {

    List<ScheduleEntity> findByUserId(UUID userId);

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM ScheduleEntity s
        WHERE s.user.id = :userId
          AND s.date = :date
          AND (
               (:start < s.endTime AND :end > s.startTime)
          )
    """)
    boolean existsOverlappingSchedule(
            @Param("userId") UUID userId,
            @Param("date") LocalDate date,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end
    );
}