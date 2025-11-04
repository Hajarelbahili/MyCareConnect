package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoAppointment.AppointmentResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.enums.AppointmentStatus;
import com.project.mycareconnect.enums.Department;
import com.project.mycareconnect.enums.Speciality;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.service.AppointmentService;
import com.project.mycareconnect.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Doctor> createDoctor(@RequestBody @Valid DoctorDto dto) {
        Doctor doctor = doctorService.createDoctor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
    }

    // ================= CURRENT DOCTOR: update self =================
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping(value = {"/update-me", "/update-me/"})
    public ResponseEntity<Doctor> updateMe(
            @Valid @ModelAttribute UserUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null); // ou un message clair
        }
        System.out.println("Mise à jour du compte connecté : " + userDetails.getUsername());
        Doctor updatedDoctor = doctorService.updateCurrentDoctor(userDetails.getUsername() ,dto);
        return ResponseEntity.ok(updatedDoctor);
    }
    // ================== Modifier un doctor (ADMIN) ==================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long doctorId,
            @Valid  @ModelAttribute  DoctorUpdateDto dto
    ) throws IOException {
        Doctor updatedDoctor = doctorService.updateDoctorByAdmin(doctorId, dto);
        return ResponseEntity.ok(updatedDoctor);
    }
    //get me
    // ================= CURRENT DOCTOR: update self =================
    @GetMapping("/getme")
    public ResponseEntity<DoctorResponseDto> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null); // ou message clair
        }

        System.out.println("Récupération du compte connecté : " + userDetails.getUsername());

        DoctorResponseDto doctor = doctorService.getMe(userDetails.getUsername());

        return ResponseEntity.ok(doctor);
    }

    // ================== Lister tous les doctors ==================

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponseDto>> searchDoctors(
            @RequestParam(required = false) Long department,
            @RequestParam(required = false) Long speciality,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Boolean active) {

        List<DoctorResponseDto> results = doctorService.searchDoctors(department, speciality, username,active);
        return results.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(results);
    }

    @GetMapping("/mes-rendez-vous")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(@AuthenticationPrincipal UserDetails userDetails
    ,     @RequestParam(required = false) AppointmentStatus status) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Récupérer le doctorId depuis le username
        Long doctorId = doctorService.getDoctorIdByUsername(userDetails.getUsername());

        List<AppointmentResponseDto> appointments = appointmentService.searchAppointments(null, doctorId, status);
        return ResponseEntity.ok(appointments);
    }
}

