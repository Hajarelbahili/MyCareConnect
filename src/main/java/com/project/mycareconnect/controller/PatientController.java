package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoAppointment.AppointmentResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoPatient.PatientDto;
import com.project.mycareconnect.dto.DtoPatient.PatientUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.enums.AppointmentStatus;
import com.project.mycareconnect.enums.Department;
import com.project.mycareconnect.enums.Speciality;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.Patient;
import com.project.mycareconnect.service.AppointmentService;
import com.project.mycareconnect.service.DoctorService;
import com.project.mycareconnect.service.PatientService;
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
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
private  final AppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('PATIENT', 'ASSISTANT')")
    @PostMapping("/create")
    public ResponseEntity<Patient> createDoctor(@RequestBody @Valid PatientDto dto) {
        Patient patient = patientService.createPatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    // ================= CURRENT DOCTOR: update self =================

    @PutMapping(value = {"/update-me", "/update-me/"})
    public ResponseEntity<Patient> updateMe(
            @Valid @ModelAttribute UserUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null); // ou un message clair
        }
        System.out.println("Mise à jour du compte connecté : " + userDetails.getUsername());
        Patient updatedPatient = patientService.updateCurrentPatient(userDetails.getUsername() ,dto);
        return ResponseEntity.ok(updatedPatient);
    }
    // ================== Modifier un PATIENT (ASSISTANT) ==================
    @PreAuthorize("hasRole('ASSISTANT')")
    @PutMapping("/update/{patientId}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long patientId,
            @Valid  @ModelAttribute PatientUpdateDto dto
    ) throws IOException {
        Patient updatedPatient = patientService.updatePatientByAdmin(patientId, dto);
        return ResponseEntity.ok(updatedPatient);
    }
    // ================== Lister tous les doctors ==================

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Boolean active
    ) {

        List<Patient> patients = patientService.searchPatients(username, active);
        return patients.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(patients);
    }

    // -------------------- DELETE USER BY ID (ADMIN) --------------------
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        patientService.deleteUser(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès");
    }

    // -------------------- DELETE CURRENT USER --------------------
    @DeleteMapping(value = {"/delete-me", "/delete-me/"})
    public ResponseEntity<String> deleteMe(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Utilisateur non authentifié !");
        }
        System.out.println("Suppression du compte connecté : " + userDetails.getUsername());
        patientService.deleteMe(userDetails);
        return ResponseEntity.ok("Compte utilisateur désactivé avec succès");
    }
    @GetMapping("/historique")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(@AuthenticationPrincipal UserDetails userDetails
            , @RequestParam(required = false) AppointmentStatus status) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Récupérer le doctorId depuis le username
        Long doctorId = patientService.getPatientIdByUsername(userDetails.getUsername());

        List<AppointmentResponseDto> appointments = appointmentService.searchAppointments(null, doctorId, status);
        return ResponseEntity.ok(appointments);
    }
}

