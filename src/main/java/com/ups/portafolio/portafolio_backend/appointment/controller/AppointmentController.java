package com.ups.portafolio.portafolio_backend.appointment.controller;

import com.ups.portafolio.portafolio_backend.appointment.entity.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.appointment.service.AppointmentService;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    private final AppointmentService appointmentService;
    
    private final UserRepository userRepository;

    @PostMapping("/book/{scheduleId}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ROLE_CLIENT', 'ADMIN', 'ROLE_ADMIN', 'USER', 'ROLE_USER')")
    public ResponseEntity<AppointmentEntity> bookAppointment(
            @PathVariable UUID scheduleId,
            @RequestBody Map<String, String> payload,
            Authentication authentication 
    ) {
        String email = authentication.getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String topic = payload.get("topic");
        return ResponseEntity.ok(appointmentService.bookAppointment(scheduleId, currentUser.getId(), topic));
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentEntity>> getMyAppointments(Authentication authentication) {
        String email = authentication.getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(appointmentService.getMyAppointments(currentUser.getId()));
    }

    @GetMapping("/incoming")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'PROGRAMMER', 'ROLE_PROGRAMMER')")
    public ResponseEntity<List<AppointmentEntity>> getIncomingAppointments(
            Authentication authentication 
    ) {
        String email = authentication.getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(appointmentService.getIncomingAppointments(currentUser.getId()));
    }

    @PutMapping("/{appointmentId}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'PROGRAMMER', 'ROLE_PROGRAMMER')")
    public ResponseEntity<AppointmentEntity> updateStatus(
            @PathVariable UUID appointmentId,
            @RequestBody Map<String, String> payload 
    ) {
        String newStatus = payload.get("status");
        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, newStatus));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@AuthenticationPrincipal UserEntity user) {
    return ResponseEntity.ok(
        appointmentService.getAppointmentSummary(user.getId())
    );
}

}