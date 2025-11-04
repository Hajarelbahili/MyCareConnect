package com.project.mycareconnect.dto.DtoAppointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.mycareconnect.enums.AppointmentStatus;
import com.project.mycareconnect.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AppointmentDto {



    @NotNull(message = "La date du rendez-vous est obligatoire.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    @NotNull(message = "L'heure d'arrivée est obligatoire.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime arrivalTime;

  

    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime actionAt;
    @NotNull
    private Long doctorId;   // obligatoire dans tous les cas

    private Long patientId;



}

