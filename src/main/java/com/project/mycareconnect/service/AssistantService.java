package com.project.mycareconnect.service;


import com.project.mycareconnect.dto.DtoAssistant.AssistantDto;
import com.project.mycareconnect.dto.DtoAssistant.AssistantResponseDto;
import com.project.mycareconnect.dto.DtoAssistant.AssistantUpdateDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.*;
import com.project.mycareconnect.repository.AssistantRepository;
import com.project.mycareconnect.repository.DepartmentRepository;
import com.project.mycareconnect.util.AssistantMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssistantService {
    private final AssistantRepository assistantRepository;
    private  final DepartmentRepository departmentRepository;
    private final AssistantMapper assistantMapper;
    private  final UserService userService;

    @Transactional
    public Assistant createAssistant(@Valid AssistantDto dto) {
        UserDto userDto = assistantMapper.toUserDto(dto);
        User savedUser = userService.saveUser(userDto); // 1️⃣
        Assistant assistant = assistantMapper.fromDto(dto, savedUser); // 2️⃣
        return assistantRepository.save(assistant); // 3️⃣
    }

    @Transactional
    public Assistant updateCurrentAssistant(String email, @Valid UserUpdateDto dto) throws IOException {
        User updatedUser = userService.updateCurrentUser(email, dto); // Mise à jour User
        Assistant assistant = assistantRepository.findByUserId(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("Docteur introuvable pour l'utilisateur " + updatedUser.getId()));
        return assistant;
    }

    // ================== Update Assistant par admin (ADMIN) ==================
    @Transactional
    public Assistant updateAssistantByAdmin(Long assistantId, @Valid AssistantUpdateDto dto) throws IOException {
        Assistant assistant = assistantRepository.findById(assistantId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + assistantId + " introuvable"));
        // Mettre à jour User associé (champs autorisés pour admin)
            User updatedUser = userService.updateUser(assistant.getUser().getId(), assistantMapper.AssistantUpdatetoUserDto(dto));

        // Mettre à jour Département
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new InvalidDataException("Département introuvable avec l'ID : " + dto.getDepartmentId()));
            assistant.setDepartment(department);
        }
// Vérifier unicité du matricule
        if (dto.getMatricule() != null) {
            boolean matriculeExists = assistantRepository.existsByMatriculeAndAssistantIdNot(dto.getMatricule(), assistantId);
            if (matriculeExists) {
                throw new InvalidDataException("Le matricule '" + dto.getMatricule() + "' existe déjà pour un autre assistant.");
            }
            assistant.setMatricule(dto.getMatricule());
        }
        //mettre a jour fonction
        if (dto.getFonction()!= null) {
            assistant.setFonction(dto.getFonction());
        }
        return assistantRepository.save(assistant);
    }

    public AssistantResponseDto getMe(String email) {
        Assistant assistant = assistantRepository.getMe(email)
                .orElseThrow(() -> new UserNotFoundException("Aucun Assistant trouvé pour l'email : " + email));

        return assistantMapper.toAssistantResponseDto(assistant);
    }
    public List<AssistantResponseDto> searchDoctors(Long department, String username, Boolean active) {
        Boolean status = active != null ? active : true;
        List<Assistant> assistants = assistantRepository.searchAssistants(department,username, status);

        return assistants.stream()
                .map(assistantMapper::toAssistantResponseDto)
                .toList();
    }
}
