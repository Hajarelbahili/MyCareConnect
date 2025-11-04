package com.project.mycareconnect.service;

import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorResponseDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.Speciality;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.repository.DepartmentRepository;
import com.project.mycareconnect.repository.DoctorRepository;
import com.project.mycareconnect.repository.SpecialityRepository;
import com.project.mycareconnect.util.DoctorMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final DoctorMapper doctorMapper;
    private  final DepartmentRepository departmentRepository;
    private final SpecialityRepository specialityRepository;

    @Transactional
    public Doctor createDoctor(@Valid DoctorDto dto) {
        UserDto userDto = doctorMapper.toUserDto(dto);
        User savedUser = userService.saveUser(userDto); // 1️⃣
        Doctor doctor = doctorMapper.fromDto(dto, savedUser); // 2️⃣
        return doctorRepository.save(doctor); // 3️⃣
    }
    @Transactional
    public Doctor updateCurrentDoctor(String email, @Valid    UserUpdateDto dto) throws IOException {
        User updatedUser = userService.updateCurrentUser(email, dto); // Mise à jour User
        Doctor doctor = doctorRepository.findByUserId(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("Docteur introuvable pour l'utilisateur " + updatedUser.getId()));
        return doctor;
    }
    // ================== Update doctor par admin (ADMIN) ==================
    @Transactional
    public Doctor updateDoctorByAdmin(Long doctorId, @Valid   DoctorUpdateDto dto) throws IOException {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + doctorId + " introuvable"));
        // Mettre à jour User associé (champs autorisés pour admin)
        User updatedUser = userService.updateUser(doctor.getUser().getId(), doctorMapper.DoctorUpdatetoUserDto(dto));

        // Mettre à jour spécialité et département
        // Mettre à jour Spécialité
        if (dto.getSpecialityId() != null) {
            Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                    .orElseThrow(() -> new InvalidDataException("Spécialité introuvable avec l'ID : " + dto.getSpecialityId()));
            doctor.setSpeciality(speciality);
        }

        // Mettre à jour Département
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new InvalidDataException("Département introuvable avec l'ID : " + dto.getDepartmentId()));
            doctor.setDepartment(department);
        }
// Mettre à jour la durée de consultation si fournie
        if (dto.getConsultationDuration() != null) {
            doctor.setConsultationDuration(dto.getConsultationDuration());
        }
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }


    public DoctorResponseDto getMe(String email) {
        Doctor doctor = doctorRepository.getMe(email)
                .orElseThrow(() -> new UserNotFoundException("Aucun docteur trouvé pour l'email : " + email));

        return doctorMapper.toDoctorResponseDto(doctor);
    }

    public List<DoctorResponseDto> searchDoctors(Long department, Long speciality, String username,Boolean active) {
        Boolean status = active != null ? active : true;
        List<Doctor> doctors = doctorRepository.searchDoctors(department, speciality, username,status);

        return doctors.stream()
                .map(doctorMapper::toDoctorResponseDto)
                .toList();
    }
    public Long getDoctorIdByUsername(String username) {
        Doctor doctor = doctorRepository.findByUser_Email(username)
                .orElseThrow(() -> new RuntimeException("Docteur non trouvé pour l'utilisateur : " + username));
        return doctor.getDoctorId();
    }
}