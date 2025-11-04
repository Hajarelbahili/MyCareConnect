package com.project.mycareconnect.controller;


import com.project.mycareconnect.dto.DtoAssistant.AssistantDto;
import com.project.mycareconnect.dto.DtoAssistant.AssistantResponseDto;
import com.project.mycareconnect.dto.DtoAssistant.AssistantUpdateDto;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomDto;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomRequest;
import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.model.Assistant;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.repository.AssistantRepository;
import com.project.mycareconnect.repository.DataRoomRepository;
import com.project.mycareconnect.repository.UserRepository;
import com.project.mycareconnect.service.AssistantService;
import com.project.mycareconnect.service.DataRoomService;
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
@RequestMapping("/api/assistants")
@RequiredArgsConstructor
public class AssistantController {
    private final AssistantService assistantService;
    private final UserRepository userRepository;
    private final AssistantRepository assistantRepository;
private  final DataRoomService dataRoomService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Assistant> createAssistant(@RequestBody @Valid AssistantDto dto) {
        Assistant assistant = assistantService.createAssistant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assistant);
    }
    // ================= CURRENT Assistant: update self =================
    @PutMapping(value = {"/update-me", "/update-me/"})
    public ResponseEntity<Assistant> updateMe(
            @Valid @ModelAttribute UserUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null); // ou un message clair
        }
        System.out.println("Mise à jour du compte connecté : " + userDetails.getUsername());
        Assistant updatedAssistant = assistantService.updateCurrentAssistant(userDetails.getUsername() ,dto);
        return ResponseEntity.ok(updatedAssistant);
    }
    // ================== Modifier un Assistant (ADMIN) ==================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{assistantId}")
    public ResponseEntity<Assistant> updateDoctor(
            @PathVariable Long assistantId,
            @Valid  @ModelAttribute AssistantUpdateDto dto
    ) throws IOException {
        Assistant updatedAssistant = assistantService.updateAssistantByAdmin(assistantId, dto);
        return ResponseEntity.ok(updatedAssistant);
    }
    @GetMapping("/getme")
    public ResponseEntity<AssistantResponseDto> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null); // ou message clair
        }

        System.out.println("Récupération du compte connecté : " + userDetails.getUsername());

        AssistantResponseDto assistant = assistantService.getMe(userDetails.getUsername());

        return ResponseEntity.ok(assistant);
    }



    @GetMapping("/search")
    public ResponseEntity<List<AssistantResponseDto>> searchDoctors(
            @RequestParam(required = false) Long department,
            @RequestParam(required = false) String username  ,
            @RequestParam(required = false) Boolean active) {

        List<AssistantResponseDto> results = assistantService.searchDoctors(department, username, active);
        return results.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(results);
    }

    @PreAuthorize("hasRole('ASSISTANT')")
    @PostMapping("/uploadDoc")
    public ResponseEntity<List<DataRoomDto>> uploadDocuments(
            @Valid @ModelAttribute DataRoomRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null); // ou un message clair
        }
        System.out.println("Mise à jour du compte connecté : " + userDetails.getUsername());
        request.setUploadedBy(assistantRepository. findByUser_Email(userDetails.getUsername()).getAssistantId());

        List<DataRoomDto> result = dataRoomService.upload(request);
        return ResponseEntity.ok(result);
    }
}


