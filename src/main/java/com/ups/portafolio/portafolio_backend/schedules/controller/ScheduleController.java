package com.ups.portafolio.portafolio_backend.schedules.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ups.portafolio.portafolio_backend.schedules.dtos.ScheduleRequest;
import com.ups.portafolio.portafolio_backend.schedules.entity.ScheduleEntity;
import com.ups.portafolio.portafolio_backend.schedules.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{userId}")
    public ResponseEntity<ScheduleEntity> create(
            @PathVariable UUID userId,
            @RequestBody ScheduleRequest request
    ) {
        return ResponseEntity.ok(
                scheduleService.createSchedule(
                        userId,
                        request.getDate(),        
                        request.getStartTime(),
                        request.getEndTime()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','PROGRAMMER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleEntity>> getByUser(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                scheduleService.getSchedulesByUser(userId)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}