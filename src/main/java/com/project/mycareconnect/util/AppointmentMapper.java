package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoAppointment.AppointmentDto;
import com.project.mycareconnect.dto.DtoAppointment.AppointmentResponseDto;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.Appointment;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.Patient;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.repository.DoctorRepository;
import com.project.mycareconnect.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppointmentMapper {

    // DTO -> Entity (sans charger les objets liés)
    public static Appointment toEntity(AppointmentDto dto, int consultationDuration) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setDate(dto.getDate());
        appointment.setArrivalTime(dto.getArrivalTime());

        appointment.setStatus(dto.getStatus());
        appointment.setActionAt(dto.getActionAt());
        // Calculer endTime automatiquement
        appointment.setEndTime(dto.getArrivalTime().plusMinutes(consultationDuration));
        appointment.setStatus(dto.getStatus());

        // 👉 On ne set PAS doctor/patient ici
        return appointment;
    }

    // Entity -> DTO
    public static AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentDto dto = new AppointmentDto();

        dto.setDate(appointment.getDate());
        dto.setArrivalTime(appointment.getArrivalTime());

        dto.setStatus(appointment.getStatus());
        dto.setActionAt(appointment.getActionAt());

        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getDoctorId());
        }
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getPatient_id());
        }

        return dto;
    }

    public AppointmentResponseDto toAppointmentResponseDto (Appointment appointment) {
        if (appointment == null) {return null;}
        AppointmentResponseDto dto = new AppointmentResponseDto();

dto.setDate(appointment.getDate());
dto.setStartTime(appointment.getArrivalTime());
dto.setEndTime(appointment.getEndTime());
dto.setStatus(appointment.getStatus());
dto.setDoctorId(appointment.getDoctor().getDoctorId());
dto.setDoctorName(appointment.getDoctor().getUser().getUsername());
dto.setDoctoremail(appointment.getDoctor().getUser().getEmail());
dto.setPatientId(appointment.getPatient().getPatient_id());
dto.setPatientemail(appointment.getPatient().getUser().getEmail());
dto.setPatientName(appointment.getPatient().getUser().getUsername());
dto.setActionAt(appointment.getActionAt());
        // Infos action

        if (appointment.getActionBy() != null) {
            dto.setActionById(appointment.getActionBy().getId());
            dto.setActionByName(appointment.getActionBy().getUsername());
        }
        return dto;
    }
}



