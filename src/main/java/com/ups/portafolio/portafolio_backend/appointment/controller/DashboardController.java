package com.ups.portafolio.portafolio_backend.appointment.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@CrossOrigin(origins = {"http://localhost:4200", "https://tu-app.onrender.com"}) 
public class DashboardController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Long>> resumen(Authentication auth) {
        if (auth == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        UserEntity user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Long> estadisticas = Map.of(
                "pendientes", appointmentService.countByProgrammerAndStatus(user.getId(), "PENDING"),
                "aprobadas", appointmentService.countByProgrammerAndStatus(user.getId(), "ACCEPTED"),
                "rechazadas", appointmentService.countByProgrammerAndStatus(user.getId(), "REJECTED")
        );
        
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> descargarPdf(Authentication auth) {
        UserEntity user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        byte[] pdfBytes = appointmentService.generatePdfReport(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Asesorias.pdf")
                .body(pdfBytes);
    }

    @GetMapping("/reporte/excel")
    public ResponseEntity<byte[]> descargarExcel(Authentication auth) {
        UserEntity user = userRepository.findByEmail(auth.getName()).orElseThrow();
        
        byte[] excelBytes = appointmentService.generateExcelReport(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Asesorias.xlsx")
                .body(excelBytes);
    }
}