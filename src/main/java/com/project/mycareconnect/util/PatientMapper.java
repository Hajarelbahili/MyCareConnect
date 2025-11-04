package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoPatient.PatientDto;
import com.project.mycareconnect.dto.DtoPatient.PatientUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.model.Patient;
import com.project.mycareconnect.model.User;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    // Convertit PatientCreationDto → UserDto
    public UserDto toUserDto(PatientDto dto) {
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
    //dtopatient to doctor
    public Patient dtotoPatient (PatientDto dto, User user){
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setBloodType(dto.getBloodType());
        return patient;
    }

    // Dans DoctorMapper
    public UserUpdateDto PatientUpdatetoUserDto(PatientUpdateDto dto) {
        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setUsername(dto.getUsername());
        userDto.setDateOfBirth(dto.getDateOfBirth());
        userDto.setPhone(dto.getPhone());
        userDto.setGender(dto.getGender());
        userDto.setImage(dto.getImage());
        return userDto;
    }
}
