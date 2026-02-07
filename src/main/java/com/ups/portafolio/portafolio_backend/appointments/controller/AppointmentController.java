package com.ups.portafolio.portafolio_backend.appointments.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ups.portafolio.portafolio_backend.appointments.dtos.CreateAppointmentDto;
import com.ups.portafolio.portafolio_backend.appointments.entity.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.appointments.services.AppointmentService;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.services.UserDetailsImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentEntity> create(
            @RequestBody CreateAppointmentDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(appointmentService.createAppointment(dto, currentUser));
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentEntity>> getMyAppointments(
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClient(currentUser.getId()));
    }

    @GetMapping("/programmer-schedule")
    public ResponseEntity<List<AppointmentEntity>> getProgrammerSchedule(
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByProgrammer(currentUser.getId()));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<AppointmentEntity> confirm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(appointmentService.confirmAppointment(id, currentUser));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<AppointmentEntity> reject(
            @PathVariable UUID id,
            @RequestBody String reason,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return ResponseEntity.ok(appointmentService.rejectAppointment(id, reason, currentUser));
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentEntity>> getByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }
}