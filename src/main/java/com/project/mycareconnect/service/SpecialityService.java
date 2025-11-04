package com.project.mycareconnect.service;

import com.project.mycareconnect.dto.DtoAvailability.AvailabilityDto;
import com.project.mycareconnect.dto.DtoSpeciality.SpecialityDto;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.*;
import com.project.mycareconnect.repository.DepartmentRepository;
import com.project.mycareconnect.repository.SpecialityRepository;
import com.project.mycareconnect.util.SpecialityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialityService {
    private final SpecialityRepository specialityRepository;
    private  final DepartmentRepository departmentRepository;
    private  final SpecialityMapper specialityMapper;

    public List<Speciality> addSpecialities(List<SpecialityDto> dtoList,Long departId) {
        Department department = departmentRepository.findById(departId)
                .orElseThrow(() -> new  InvalidDataException("Département introuvable avec l'ID : " + departId));

// Vérifier les doublons dans la liste reçue
        List<String> namesInRequest = dtoList.stream()
                .map(SpecialityDto::getName)
                .toList();
        if (namesInRequest.size() != namesInRequest.stream().distinct().count()) {
            throw new InvalidDataException("La liste contient des spécialités en double.");
        }
        // Vérifier les doublons dans la base pour ce département
        for (SpecialityDto dto : dtoList) {
            if (specialityRepository.existsByNameAndDepartment(dto.getName(), department)) {
                throw new InvalidDataException(
                        "La spécialité '" + dto.getName() + "' existe déjà dans ce département."
                );
            }
        }
        List<Speciality> specialities = dtoList.stream().map(dto -> {
            Speciality speciality = specialityMapper.fromDto(dto);
            speciality.setDepartment(department);

            return speciality;
        }).toList();

        return specialityRepository.saveAll(specialities);
    }

    public Speciality updateSpeciality(Long specialityId, SpecialityDto dto) {
        Speciality speciality = specialityRepository.findById(specialityId)
                .orElseThrow(() -> new InvalidDataException("Spécialité introuvable avec l'ID : " + specialityId));

        // Vérifier que le nouveau nom n'existe pas déjà dans le même département
        if (specialityRepository.existsByNameAndDepartmentAndSpecialityIdNot(
                dto.getName(),
                speciality.getDepartment(),
                speciality.getSpecialityId() // ID de la spécialité en cours
        )) {
            throw new InvalidDataException(
                    "La spécialité '" + dto.getName() + "' existe déjà dans ce département."
            );
        }

        // Mettre à jour les champs
        speciality.setName(dto.getName());
        speciality.setDescription(dto.getDescription());

        return specialityRepository.save(speciality);
    }
    public Speciality setSpecialityStatus(Long specialityId, boolean status) {
        Speciality speciality = specialityRepository.findById(specialityId)
                .orElseThrow(() -> new InvalidDataException("Spécialité introuvable"));
        speciality.setActive(status);
        return specialityRepository.save(speciality);
    }

    public List<Speciality> searchSpecialities(
            String speciality,
            String department,
            Boolean active) {
        Boolean status = (active != null) ? active : true;
        return specialityRepository.searchSpecialities(speciality, department, status);
    }
}
