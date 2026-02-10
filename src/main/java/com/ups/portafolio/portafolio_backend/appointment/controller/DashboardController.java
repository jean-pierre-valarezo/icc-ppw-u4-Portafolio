package com.ups.portafolio.portafolio_backend.appointment.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ups.portafolio.portafolio_backend.appointment.service.AppointmentService;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/programmer/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @GetMapping("/resumen")
    public Map<String, Long> resumen(Authentication auth) {
        String email = auth.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow();

        return Map.of(
                "pendientes", appointmentService.countByProgrammerAndStatus(user.getId(), "PENDING"),
                "aprobadas", appointmentService.countByProgrammerAndStatus(user.getId(), "ACCEPTED"),
                "rechazadas", appointmentService.countByProgrammerAndStatus(user.getId(), "REJECTED")
        );
    }
     @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> pdf() {
        byte[] content = "Reporte de asesorías".getBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte.pdf")
                .body(content);
    }


    @GetMapping("/reporte/excel")
    public ResponseEntity<byte[]> excel() {
        byte[] content = "Reporte Excel".getBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte.xlsx")
                .body(content);
    }
}
