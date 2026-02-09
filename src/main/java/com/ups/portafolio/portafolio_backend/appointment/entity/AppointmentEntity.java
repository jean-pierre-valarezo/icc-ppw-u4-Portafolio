package com.ups.portafolio.portafolio_backend.appointment.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ups.portafolio.portafolio_backend.schedules.entity.ScheduleEntity;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private UserEntity client; 

    @ManyToOne
    @JoinColumn(name = "programmer_id")
    private UserEntity programmer; 

    @OneToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule; 

    @Builder.Default
    private String status = "PENDING"; 

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 500) 
    private String topic;
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UserEntity getClient() {
        return client;
    }
    public void setClient(UserEntity client) {
        this.client = client;
    }
    public UserEntity getProgrammer() {
        return programmer;
    }
    public void setProgrammer(UserEntity programmer) {
        this.programmer = programmer;
    }
    public ScheduleEntity getSchedule() {
        return schedule;
    }
    public void setSchedule(ScheduleEntity schedule) {
        this.schedule = schedule;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}