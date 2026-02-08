package com.ups.portafolio.portafolio_backend.schedules.service;

import com.ups.portafolio.portafolio_backend.schedules.dtos.ScheduleRequest;
import com.ups.portafolio.portafolio_backend.schedules.entity.ScheduleEntity;
import com.ups.portafolio.portafolio_backend.schedules.repository.ScheduleRepository;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;
import com.ups.portafolio.portafolio_backend.users.roles.Role;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleEntity createSchedule(
            UUID userId,
            LocalDate date,
            LocalTime start,
            LocalTime end
    ) {
        if (!end.isAfter(start)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El horario no es válido"
            );
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));

        if (!Role.PROGRAMMER.equals(user.getRole())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Solo los programadores pueden tener horarios"
            );
        }

        if (scheduleRepository.existsOverlappingSchedule(userId, date, start, end)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El horario se cruza con otro existente"
            );
        }

        ScheduleEntity schedule = ScheduleEntity.builder()
                .user(user)
                .date(date)
                .startTime(start)
                .endTime(end)
                .available(true)
                .build();

        return scheduleRepository.save(schedule);
    }

    public List<ScheduleEntity> getSchedulesByUser(UUID userId) {
        return scheduleRepository.findByUserId(userId);
    }

    public void deleteSchedule(UUID scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}
