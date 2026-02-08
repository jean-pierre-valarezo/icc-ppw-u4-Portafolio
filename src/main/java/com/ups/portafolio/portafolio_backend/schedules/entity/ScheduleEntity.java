package com.ups.portafolio.portafolio_backend.schedules.entity;

import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "schedules")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private boolean available; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}