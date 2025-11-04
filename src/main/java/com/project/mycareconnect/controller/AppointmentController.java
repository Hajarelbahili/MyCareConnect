package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoAppointment.AppointmentDto;
import com.project.mycareconnect.dto.DtoAppointment.AppointmentResponseDto;
import com.project.mycareconnect.dto.DtoAppointment.ChangeStatusRequest;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.enums.AppointmentStatus;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/can-book")
    public ResponseEntity<Boolean> canBook(
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime
    ) {
        boolean result = appointmentService.checkAvailability(doctorId, patientId, date, startTime);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/create")
    public ResponseEntity<AppointmentDto> createAppointment(
            @Valid @RequestBody AppointmentDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null); // ou un message clair
        }
        System.out.println("Mise à jour du compte connecté : " + userDetails.getUsername());
        AppointmentDto created = appointmentService.createAppointment(userDetails.getUsername(), dto);
        return ResponseEntity.ok(created);
    }
    @PreAuthorize("hasRole('ASSISTANT')")
    @PostMapping("/createByassistant")
    public ResponseEntity<AppointmentDto> createAppointmentByassistant(
            @Valid @RequestBody AppointmentDto dto
    ) {
        AppointmentDto created = appointmentService.createAppointmentByAssistant(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AppointmentResponseDto>> searchAppointments(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) AppointmentStatus status
    ) {
        List<AppointmentResponseDto> result = appointmentService.searchAppointments(patientId, doctorId, status);
        return ResponseEntity.ok(result);
    }



    @PutMapping("/changestatus")
    public ResponseEntity<AppointmentResponseDto> changeStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangeStatusRequest request
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null);
        }

        AppointmentResponseDto updated = appointmentService.changeStatusAppointment(
                userDetails.getUsername(),
                request.getStatus(),
                request.getAppointmentId()
        );
        return ResponseEntity.ok(updated);
    }


}
