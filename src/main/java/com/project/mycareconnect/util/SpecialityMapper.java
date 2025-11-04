package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoSpeciality.SpecialityDto;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Speciality;
import com.project.mycareconnect.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor // <- Lombok va générer un constructeur avec tous les final fields
public class SpecialityMapper {

    private final DepartmentRepository departmentRepository;

    // Transforme un DTO en entité Speciality
    public Speciality fromDto(SpecialityDto dto) {
        Speciality speciality = new Speciality();
        speciality.setName(dto.getName());
        speciality.setDescription(dto.getDescription());



        return speciality;
    }

    // Transforme une entité Speciality en DTO
    public SpecialityDto toDto(Speciality speciality) {
        SpecialityDto dto = new SpecialityDto();
        dto.setName(speciality.getName());
        dto.setDescription(speciality.getDescription());

        return dto;
    }
}

