package com.ups.portafolio.portafolio_backend.appointment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ups.portafolio.portafolio_backend.appointment.entity.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.appointment.repository.AppointmentRepository;
import com.ups.portafolio.portafolio_backend.schedules.entity.ScheduleEntity;
import com.ups.portafolio.portafolio_backend.schedules.repository.ScheduleRepository;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public AppointmentEntity bookAppointment(UUID scheduleId, UUID clientId, String topic) {
        
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        if (!"AVAILABLE".equals(schedule.getStatus())) {
            throw new RuntimeException("Este horario ya no está disponible");
        }

        UserEntity client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        AppointmentEntity appointment = AppointmentEntity.builder()
                .schedule(schedule)
                .client(client)
                .programmer(schedule.getUser()) 
                .status("PENDING") 
                .topic(topic)      
                .build();

        schedule.setStatus("BOOKED");
        scheduleRepository.save(schedule);

        return appointmentRepository.save(appointment);
    }

    public List<AppointmentEntity> getMyAppointments(UUID clientId) {
        return appointmentRepository.findByClientId(clientId);
    }
}