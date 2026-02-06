package com.ups.portafolio.portafolio_backend.users.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.ups.portafolio.portafolio_backend.users.entities.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.services.AppointmentService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public AppointmentEntity createAppointment(@RequestBody AppointmentEntity appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @PostMapping("/programmer")
    public List<AppointmentEntity> getByProgrammer(@RequestBody UserEntity programmer) {
        return appointmentService.getAppointmentsByProgrammer(programmer);
    }

    @PostMapping("/client")
    public List<AppointmentEntity> getByClient(@RequestBody UserEntity client) {
        return appointmentService.getAppointmentsByClient(client);
    }

    @GetMapping("/date/{date}")
    public List<AppointmentEntity> getByDate(@PathVariable LocalDate date) {
        return appointmentService.getAppointmentsByDate(date);
    }

    @PutMapping("/{id}/confirm")
    public AppointmentEntity confirm(@PathVariable UUID id) {
        return appointmentService.confirmAppointment(id);
    }

    @PutMapping("/{id}/reject")
    public AppointmentEntity reject(
            @PathVariable UUID id,
            @RequestBody String reason
    ) {
        return appointmentService.rejectAppointment(id, reason);
    }
}
