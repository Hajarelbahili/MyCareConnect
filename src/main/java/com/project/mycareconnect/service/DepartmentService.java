package com.project.mycareconnect.service;

import com.project.mycareconnect.dto.DtoDepartment.DepartmentDto;
import com.project.mycareconnect.exception.FileProcessingException;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.repository.DepartmentRepository;
import com.project.mycareconnect.util.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentMapper departmentMapper;
    private  final DepartmentRepository departmentRepository;

    public Department createDepartment(DepartmentDto dto) {
        Department department = departmentMapper.fromDepartmnetDto(dto);
        if (departmentRepository.existsByName(department.getName())) {
            throw new InvalidDataException("Un département avec ce nom existe déjà !");
        }

        MultipartFile imageFile = dto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {

            long maxBytes = 2 * 1024 * 1024;
            if (imageFile.getSize() > maxBytes) {
                throw new InvalidDataException("L'image dépasse la taille maximale autorisée (2 Mo).");
            }

            String contentType = imageFile.getContentType();
            if (contentType == null ||
                    !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                throw new InvalidDataException("Format d'image non supporté. Autorisés : JPEG, PNG.");
            }

            try {
                department.setImageData(imageFile.getBytes());
                department.setImageName(imageFile.getOriginalFilename());
                department.setImageType(contentType);
            } catch (IOException e) {
                throw new FileProcessingException("Impossible de lire l'image envoyée.", e);
            }
        }

        return departmentRepository.save(department);
    }
    public Department updateDepartment(Long departmentId, DepartmentDto dto) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new InvalidDataException("Département introuvable avec l'id : " + departmentId));

        // Vérifier si le nouveau nom existe déjà dans un autre département
        if (dto.getName() != null &&
                departmentRepository.existsByNameAndDepartmentIdNot(dto.getName(), departmentId)) {
            throw new InvalidDataException("Un département avec ce nom existe déjà !");
        }

        department =departmentMapper.fromDepartmnetDtoupdated(dto,department);

        // Gestion de l'image
        MultipartFile imageFile = dto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            long maxBytes = 2 * 1024 * 1024; // 2 Mo
            if (imageFile.getSize() > maxBytes) {
                throw new InvalidDataException("L'image dépasse la taille maximale autorisée (2 Mo).");
            }

            String contentType = imageFile.getContentType();
            if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                throw new InvalidDataException("Format d'image non supporté. Autorisés : JPEG, PNG.");
            }

            try {
                department.setImageData(imageFile.getBytes());
                department.setImageName(imageFile.getOriginalFilename());
                department.setImageType(contentType);
            } catch (IOException e) {
                throw new FileProcessingException("Impossible de lire l'image envoyée.", e);
            }
        }

        // Sauvegarder et retourner le département mis à jour
        return departmentRepository.save(department);
    }
    // ================= DELETE =================
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new InvalidDataException("Département introuvable avec l'id : " + departmentId));

        // Optionnel : vérifier si des spécialités sont liées et décider du comportement
        if (department.getSpecialities() != null && !department.getSpecialities().isEmpty()) {
            throw new InvalidDataException("Impossible de supprimer ce département car il contient des spécialités.");
        }

        departmentRepository.delete(department);
    }
    public Department setDepartmentStatus(Long departmentId, boolean status) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new InvalidDataException("Département introuvable"));
        department.setActive(status);
        return departmentRepository.save(department);
    }

    public List<Department> searchDepartments(String name, Boolean active) {
        Boolean status = active != null ? active : true;
        return departmentRepository.searchDepartments(name, status);
    }
}
