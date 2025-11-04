package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoSpeciality.SpecialityDto;
import com.project.mycareconnect.dto.DtoSpeciality.SpecialityRequest;
import com.project.mycareconnect.model.Speciality;
import com.project.mycareconnect.service.SpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialities")
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService specialityService;

    // ➕ Ajouter plusieurs spécialités dans un département

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/department/{departId}")
    public ResponseEntity<List<Speciality>> addSpecialities(
            @PathVariable Long departId,
            @RequestBody @Valid SpecialityRequest request) {
        List<Speciality> saved = specialityService.addSpecialities(request.getSpecialities(),departId);
        return ResponseEntity.ok(saved);
    }

    // ✏️ Mettre à jour une spécialité (par ID)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{specialityId}")
    public ResponseEntity<Speciality> updateSpeciality(
            @PathVariable Long specialityId,
            @RequestBody SpecialityDto dto) {
        return ResponseEntity.ok(specialityService.updateSpeciality(specialityId, dto));
    }

    // 🗑️ Activer/Désactiver une spécialité (soft delete ou désactivation)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/changestatus/{specialityId}/status")
    public ResponseEntity<Speciality> setSpecialityStatus(
            @PathVariable Long specialityId,
            @RequestParam boolean status) {
        return ResponseEntity.ok(specialityService.setSpecialityStatus(specialityId, status));
    }

    // 🔍 Rechercher des spécialités avec filtres facultatifs
    @GetMapping("/search")
    public ResponseEntity<List<Speciality>> searchSpecialities(
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(specialityService.searchSpecialities(speciality, department, active));
    }
}
