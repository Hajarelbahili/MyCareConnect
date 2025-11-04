package com.project.mycareconnect.dto.DtoAppointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.mycareconnect.enums.AppointmentStatus;
import com.project.mycareconnect.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AppointmentResponseDto {
    //Infos appointment
    private Long appointmentId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime  endTime;
    private AppointmentStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime actionAt;
    private Long actionById;
    private String actionByName;
    // Infos Patient
    private Long patientId;
    private String patientName;
    private String patientemail;
    // Infos doctor
    private Long doctorId;
    private String doctorName;
    private String doctoremail;


}
