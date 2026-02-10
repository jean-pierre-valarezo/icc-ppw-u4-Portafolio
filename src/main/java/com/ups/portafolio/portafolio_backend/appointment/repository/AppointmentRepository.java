package com.ups.portafolio.portafolio_backend.appointment.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ups.portafolio.portafolio_backend.appointment.entity.AppointmentEntity;


@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findByClientId(UUID clientId);
    
    List<AppointmentEntity> findByProgrammerId(UUID programmerId);

    long countByProgrammerIdAndStatus(UUID programmerId, String status);
}