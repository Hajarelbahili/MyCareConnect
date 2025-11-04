package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoAssistant.AssistantDto;
import com.project.mycareconnect.dto.DtoAssistant.AssistantResponseDto;
import com.project.mycareconnect.dto.DtoAssistant.AssistantUpdateDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.model.*;
import com.project.mycareconnect.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AssistantMapper {
    private final UserMapper userMapper;
    private final DepartmentRepository departmentRepository;


    public Assistant fromDto(AssistantDto dto, User user) {
        Assistant assistant = new Assistant();
        assistant.setUser(user);
        //department
        Department department=departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new InvalidDataException("Département introuvable avec l'ID : " + dto.getDepartmentId()));
        assistant.setDepartment(department);

assistant.setMatricule(dto.getMatricule());
assistant.setFonction(dto.getFonction());
        return assistant;
    }

    // Convertit AssistantDto → UserDto
    public UserDto toUserDto(AssistantDto dto) {
        UserDto userDto = new UserDto();
        userDto.setUsername(dto.getUsername());
        userDto.setEmail(dto.getEmail());
        userDto.setPassword(dto.getPassword());
        userDto.setDateOfBirth(dto.getDateOfBirth());
        userDto.setPhone(dto.getPhone());
        userDto.setRole(dto.getRole());
        userDto.setGender(dto.getGender());
        return userDto;
    }

    // Dans DoctorMapper
    public UserUpdateDto AssistantUpdatetoUserDto(AssistantUpdateDto dto) {
        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setUsername(dto.getUsername());
        userDto.setDateOfBirth(dto.getDateOfBirth());
        userDto.setPhone(dto.getPhone());
        userDto.setGender(dto.getGender());
        userDto.setImage(dto.getImage());
        return userDto;
    }
    public AssistantResponseDto toAssistantResponseDto(Assistant assistant) {
        AssistantResponseDto dto = new AssistantResponseDto();

        // Infos User
        dto.setUserId(assistant.getUser().getId());
        dto.setUsername(assistant.getUser().getUsername());
        dto.setEmail(assistant.getUser().getEmail());
        dto.setDateOfBirth(assistant.getUser().getDateOfBirth());
        dto.setPhone(assistant.getUser().getPhone());
        dto.setGender(assistant.getUser().getGender() != null ? assistant.getUser().getGender().name() : null);



        // Infos Department
        if (assistant.getDepartment() != null) {
            dto.setDepartmentId(assistant.getDepartment().getDepartmentId());
            dto.setDepartmentName(assistant.getDepartment().getName());
        }
        // Infos Department
        if (assistant.getFonction()!= null) {
           dto.setFonction(assistant.getFonction());
        }
        if (assistant.getMatricule()!= null) {
           dto.setMatricul(assistant.getMatricule());
        }

        return dto;
    }

}
