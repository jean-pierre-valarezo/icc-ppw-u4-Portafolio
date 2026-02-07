package com.ups.portafolio.portafolio_backend.appointments.services;

import com.ups.portafolio.portafolio_backend.appointments.dtos.CreateAppointmentDto;
import com.ups.portafolio.portafolio_backend.appointments.entity.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.users.services.UserDetailsImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    AppointmentEntity createAppointment(CreateAppointmentDto dto, UserDetailsImpl currentUser);

    AppointmentEntity confirmAppointment(UUID id, UserDetailsImpl currentUser);

    AppointmentEntity rejectAppointment(UUID id, String reason, UserDetailsImpl currentUser);

    List<AppointmentEntity> getAppointmentsByClient(UUID clientId);

    List<AppointmentEntity> getAppointmentsByProgrammer(UUID programmerId);

    List<AppointmentEntity> getAppointmentsByDate(LocalDate date);
}
