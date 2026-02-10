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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'PROGRAMMER', 'ROLE_PROGRAMMER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleEntity>> getByUser(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                scheduleService.getSchedulesByUser(userId)
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/programmer/{programmerId}/available")
        public ResponseEntity<List<ScheduleEntity>> getAvailableByProgrammer(
                @PathVariable UUID programmerId
        ) {
        return ResponseEntity.ok(
                scheduleService.getAvailableSchedulesByProgrammer(programmerId)
        );
        }
}