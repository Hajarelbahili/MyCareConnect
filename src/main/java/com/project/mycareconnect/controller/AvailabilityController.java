package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoAvailability.AvailabilityRequest;
import com.project.mycareconnect.model.Availability;
import com.project.mycareconnect.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/bulk-add")
    public ResponseEntity<List<Availability>> addAvailabilities(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid AvailabilityRequest request) {

        List<Availability> savedAvailabilities =
                availabilityService.addAvailabilities(userDetails, request.getAvailabilities());

        return ResponseEntity.ok(savedAvailabilities);
    }
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/me")
    public ResponseEntity<List<Availability>> getMyAvailabilities(@AuthenticationPrincipal UserDetails userDetails) {

        List<Availability> availabilities = availabilityService.getDoctorAvailabilities(userDetails);
        return ResponseEntity.ok(availabilities);
    }
}

