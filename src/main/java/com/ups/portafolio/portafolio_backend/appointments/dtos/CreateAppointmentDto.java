package com.ups.portafolio.portafolio_backend.appointments.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class CreateAppointmentDto {
    @NotNull(message = "Debes indicar el ID del programador")
    UUID programmerId;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La cita debe ser en el futuro")
    LocalDate date;

    @NotNull(message = "La hora de inicio es obligatoria")
    LocalTime startTime;

    @NotNull(message = "La modalidad es obligatoria")
    String modality;

    public UUID getProgrammerId() {
        return programmerId;
    }

    public void setProgrammerId(UUID programmerId) {
        this.programmerId = programmerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    
}
