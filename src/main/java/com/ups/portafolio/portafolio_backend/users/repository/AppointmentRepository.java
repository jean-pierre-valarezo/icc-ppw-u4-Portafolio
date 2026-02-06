package com.ups.portafolio.portafolio_backend.users.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.ups.portafolio.portafolio_backend.users.entities.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

   List<AppointmentEntity> findByProgrammer(UserEntity programmer);

    List<AppointmentEntity> findByClient(UserEntity client);

    List<AppointmentEntity> findByDate(LocalDate date);
    
}
