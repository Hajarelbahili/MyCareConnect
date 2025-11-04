package com.project.mycareconnect.service;

import com.project.mycareconnect.dto.DtoAppointment.AppointmentDto;
import com.project.mycareconnect.dto.DtoAppointment.AppointmentResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorResponseDto;
import com.project.mycareconnect.enums.AppointmentStatus;
import com.project.mycareconnect.enums.Role;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.*;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.repository.*;
import com.project.mycareconnect.util.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;
private  final AssistantRepository assistantRepository;
    public boolean checkAvailability(Long doctorId, Long patientId, LocalDate date, LocalTime startTime) {
//Long doctorId, Long patientId, LocalDate date, LocalTime startTime
        // Vérification que la date/heure est future
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (date.isBefore(today) || (date.isEqual(today) && !startTime.isAfter(now))) {
            throw new InvalidDataException("La date et l'heure du rendez-vous doivent être postérieures à l'heure actuelle.");
        }
        return appointmentRepository.canBook(doctorId, patientId, date, startTime);
    }


    public AppointmentDto createAppointment(String email, AppointmentDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Docteur avec ID " + dto.getDoctorId() + " introuvable"));
       User user=userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Docteur introuvable pour l'utilisateur " ));

        Patient patient = patientRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Docteur introuvable pour l'utilisateur " + user.getId()));
        int consultationDuration = doctor.getConsultationDuration();
        // 4️⃣ Vérifier la disponibilité
        boolean canBook = checkAvailability(doctor.getDoctorId(), patient.getPatient_id(), dto.getDate(), dto.getArrivalTime());
        if (!canBook) {
            throw new InvalidDataException("Ce créneau n'est pas disponible pour le docteur " + doctor.getDoctorId());
        }

        // 5️⃣ Créer et persister le rendez-vous
        Appointment appointment = AppointmentMapper.toEntity(dto,consultationDuration);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        Appointment saved = appointmentRepository.save(appointment);

        // 6️⃣ Retourner en DTO
        return AppointmentMapper.toDto(saved);
    }
    public AppointmentDto createAppointmentByAssistant(AppointmentDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Docteur avec ID " + dto.getDoctorId() + " introuvable"));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Docteur introuvable pour l'utilisateur " + dto.getPatientId()));
        int consultationDuration = doctor.getConsultationDuration();
        // 4️⃣ Vérifier la disponibilité
        boolean canBook = checkAvailability(doctor.getDoctorId(), patient.getPatient_id(), dto.getDate(), dto.getArrivalTime());
        if (!canBook) {
            throw new InvalidDataException("Ce créneau n'est pas disponible pour le docteur " + doctor.getDoctorId());
        }

        // 5️⃣ Créer et persister le rendez-vous
        Appointment appointment = AppointmentMapper.toEntity(dto,consultationDuration);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        Appointment saved = appointmentRepository.save(appointment);

        // 6️⃣ Retourner en DTO
        return AppointmentMapper.toDto(saved);
    }
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> searchAppointments(
                                                   Long patientId,
                                                          Long doctorId,
                                                        AppointmentStatus status) {

        List<Appointment> appointments = appointmentRepository.searchAppointments(patientId, doctorId,status);

        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponseDto)
                .toList();
    }

    public  AppointmentResponseDto changeStatusAppointment(String email,AppointmentStatus status,Long appointmentId){
User user= userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("Docteur avec ID " + email + " introuvable"));

Appointment appointment =appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new InvalidDataException("Docteur avec ID " + user.getId() + " introuvable"));
        appointment.setStatus(status);
        appointment.setActionAt(LocalDateTime.now());
        appointment.setActionBy(user);
        return appointmentMapper.toAppointmentResponseDto(appointmentRepository.save(appointment));
    }
}
