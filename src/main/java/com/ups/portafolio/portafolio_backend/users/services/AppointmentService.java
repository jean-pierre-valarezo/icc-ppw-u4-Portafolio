package com.ups.portafolio.portafolio_backend.users.services;



import com.ups.portafolio.portafolio_backend.users.entities.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentEntity createAppointment(AppointmentEntity appointment) {
        appointment.setStatus("CREATED");
        return appointmentRepository.save(appointment);
    }

    public List<AppointmentEntity> getAppointmentsByProgrammer(UserEntity programmer) {
    return appointmentRepository.findByProgrammer(programmer);
}

public List<AppointmentEntity> getAppointmentsByClient(UserEntity client) {
    return appointmentRepository.findByClient(client);
}


    public List<AppointmentEntity> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByDate(date);
    }

    public AppointmentEntity confirmAppointment(UUID id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asesoria no encontrada"));

        appointment.setStatus("CONFIRMED");
        return appointmentRepository.save(appointment);
    }

    public AppointmentEntity rejectAppointment(UUID id, String reason) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asesoria no encontrada"));

        appointment.setStatus("REJECTED");
        appointment.setRejectionReason(reason);
        return appointmentRepository.save(appointment);
    }
}
