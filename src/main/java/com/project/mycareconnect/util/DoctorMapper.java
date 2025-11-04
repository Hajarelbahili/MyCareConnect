package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.Speciality;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.repository.DepartmentRepository;
import com.project.mycareconnect.repository.SpecialityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

    private  final DepartmentRepository departmentRepository;
    private final SpecialityRepository specialityRepository;

    // Convertit DoctorCreationDto + User déjà créé → Doctor
    public Doctor fromDto(DoctorDto dto, User user) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        //department
        Department department=departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new InvalidDataException("Département introuvable avec l'ID : " + dto.getDepartmentId()));
        doctor.setDepartment(department);

     //speciality
        Speciality speciality=specialityRepository.findById(dto.getSpecialityId())
                .orElseThrow(() -> new InvalidDataException("speciality introuvable avec l'ID : " + dto.getSpecialityId()));
        doctor.setSpeciality(speciality);
        // ✅ Durée consultation
        doctor.setConsultationDuration(dto.getConsultationDuration());
        return doctor;
    }

    // Convertit DoctorCreationDto → UserDto
    public UserDto toUserDto(DoctorDto dto) {
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
    public UserUpdateDto DoctorUpdatetoUserDto(DoctorUpdateDto dto) {
        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setUsername(dto.getUsername());
        userDto.setDateOfBirth(dto.getDateOfBirth());
        userDto.setPhone(dto.getPhone());
        userDto.setGender(dto.getGender());
       userDto.setImage(dto.getImage());
        return userDto;
    }

    // Si besoin : Entity → DTO pour retour API
    public DoctorDto toDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setUsername(doctor.getUser().getUsername());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setDateOfBirth(doctor.getUser().getDateOfBirth());
        dto.setPhone(doctor.getUser().getPhone());
        dto.setRole(doctor.getUser().getRole());
        dto.setGender(doctor.getUser().getGender());

        return dto;
    }
    public DoctorResponseDto toDoctorResponseDto(Doctor doctor) {
        DoctorResponseDto dto = new DoctorResponseDto();

        // Infos User
        dto.setUserId(doctor.getUser().getId());
        dto.setUsername(doctor.getUser().getUsername());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setDateOfBirth(doctor.getUser().getDateOfBirth());
        dto.setPhone(doctor.getUser().getPhone());
        dto.setGender(doctor.getUser().getGender() != null ? doctor.getUser().getGender().name() : null);

        // Infos Speciality
        if (doctor.getSpeciality() != null) {
            dto.setSpecialityId(doctor.getSpeciality().getSpecialityId());
            dto.setSpecialityName(doctor.getSpeciality().getName());
        }

        // Infos Department
        if (doctor.getDepartment() != null) {
            dto.setDepartmentId(doctor.getDepartment().getDepartmentId());
            dto.setDepartmentName(doctor.getDepartment().getName());
        }

        // ✅ Ajouter durée consultation
        dto.setConsultationDuration(doctor.getConsultationDuration());

        return dto;
    }

}