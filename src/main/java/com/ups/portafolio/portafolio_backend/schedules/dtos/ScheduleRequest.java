package com.ups.portafolio.portafolio_backend.schedules.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ScheduleRequest {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
