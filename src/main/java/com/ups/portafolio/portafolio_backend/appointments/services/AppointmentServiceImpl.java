package com.ups.portafolio.portafolio_backend.appointments.services;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.ups.portafolio.portafolio_backend.appointments.dtos.CreateAppointmentDto;
import com.ups.portafolio.portafolio_backend.appointments.entity.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.appointments.repository.AppointmentRepository;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;
import com.ups.portafolio.portafolio_backend.users.services.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AppointmentEntity createAppointment(CreateAppointmentDto dto, UserDetailsImpl currentUser) {
        
        UserEntity client = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        UserEntity programmer = userRepository.findById(dto.getProgrammerId()) 
                .orElseThrow(() -> new RuntimeException("Programador no encontrado"));

        LocalTime endTime = dto.getStartTime().plusHours(1); 

        boolean isOccupied = appointmentRepository.existsOverlap(
                programmer.getId(), 
                dto.getDate(),      
                dto.getStartTime(), 
                endTime
        );

        if (isOccupied) {
            throw new RuntimeException("El horario seleccionado ya está ocupado.");
        }

        AppointmentEntity appointment = AppointmentEntity.builder()
                .client(client)
                .programmer(programmer)
                .date(dto.getDate())           
                .startTime(dto.getStartTime()) 
                .endTime(endTime)
                .modality(dto.getModality()) 
                .status("CREATED")
                .build();

        AppointmentEntity saved = appointmentRepository.save(appointment);
        sendNotificationMock(programmer.getEmail(), "Nueva solicitud de asesoría pendiente.");
        return saved;
    }

    @Override
    @Transactional
    public AppointmentEntity confirmAppointment(UUID id, UserDetailsImpl currentUser) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        validateProgrammerOwnership(appointment, currentUser);

        appointment.setStatus("CONFIRMED");
        sendNotificationMock(appointment.getClient().getEmail(), 
            "¡Tu cita ha sido CONFIRMADA! Nos vemos el " + appointment.getDate());
            
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public AppointmentEntity rejectAppointment(UUID id, String reason, UserDetailsImpl currentUser) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        validateProgrammerOwnership(appointment, currentUser);

        appointment.setStatus("REJECTED");
        appointment.setRejectionReason(reason);

        sendNotificationMock(appointment.getClient().getEmail(), 
            "Tu cita fue rechazada. Motivo: " + reason);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentEntity> getAppointmentsByClient(UUID clientId) {
        UserEntity client = userRepository.findById(clientId).orElseThrow();
        return appointmentRepository.findByClient(client);
    }

    @Override
    public List<AppointmentEntity> getAppointmentsByProgrammer(UUID programmerId) {
        UserEntity programmer = userRepository.findById(programmerId).orElseThrow();
        return appointmentRepository.findByProgrammer(programmer);
    }
    
    @Override
    public List<AppointmentEntity> getAppointmentsByDate(java.time.LocalDate date) {
        return appointmentRepository.findByDate(date);
    }

    private void validateProgrammerOwnership(AppointmentEntity appointment, UserDetailsImpl currentUser) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !appointment.getProgrammer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para gestionar esta cita.");
        }
    }

    private void sendNotificationMock(String email, String message) {
        System.out.println("SERVICIO NOTIFICACION");
        System.out.println("Para: " + email);
        System.out.println("Mensaje: " + message);
        System.out.println("Status: ENVIADO");
    }
}
