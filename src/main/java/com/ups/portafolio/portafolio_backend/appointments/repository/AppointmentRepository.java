package com.ups.portafolio.portafolio_backend.appointments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ups.portafolio.portafolio_backend.appointments.entity.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findByProgrammer(UserEntity programmer);
    List<AppointmentEntity> findByClient(UserEntity client);
    List<AppointmentEntity> findByDate(LocalDate date);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a " +
           "WHERE a.programmer.id = :programmerId " +
           "AND a.date = :date " +
           "AND a.status != 'REJECTED' " + 
           "AND (:startTime < a.endTime AND :endTime > a.startTime)")
    boolean existsOverlap(
            @Param("programmerId") UUID programmerId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
