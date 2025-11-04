package com.project.mycareconnect.service;

import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorUpdateDto;
import com.project.mycareconnect.dto.DtoPatient.PatientDto;
import com.project.mycareconnect.dto.DtoPatient.PatientUpdateDto;
import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.enums.Department;
import com.project.mycareconnect.enums.Speciality;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.Patient;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.repository.DoctorRepository;
import com.project.mycareconnect.repository.PatientRepository;
import com.project.mycareconnect.util.DoctorMapper;
import com.project.mycareconnect.util.PatientMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository ;
    private final UserService userService;
    private final PatientMapper patientMapper;

    @Transactional
    public Patient createPatient(@Valid PatientDto dto) {
        UserDto userDto = patientMapper.toUserDto(dto);
        User savedUser = userService.saveUser(userDto); // 1️⃣
        Patient patient = patientMapper.dtotoPatient(dto, savedUser); // 2️⃣
        return patientRepository.save(patient); // 3️⃣
    }
    @Transactional
    public Patient updateCurrentPatient(String email, @Valid UserUpdateDto dto) throws IOException {
        User updatedUser = userService.updateCurrentUser(email, dto); // Mise à jour User
        Patient patient = patientRepository.findByUser_Id(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("Docteur introuvable pour l'utilisateur " + updatedUser.getId()));
        return patient;
    }
    // ================== Update doctor par admin (ADMIN) ==================
    @Transactional
    public Patient updatePatientByAdmin(Long patientId, @Valid PatientUpdateDto dto) throws IOException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + patientId + " introuvable"));
        // Mettre à jour User associé (champs autorisés pour admin)
        User updatedUser = userService.updateUser(patient.getUser().getId(), patientMapper.PatientUpdatetoUserDto(dto));


        if(dto.getBloodType() != null) patient.setBloodType(dto.getBloodType());


        return patientRepository.save(patient);
    }


    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> searchPatients(String username , Boolean active) {
        Boolean status = active != null ? active : true; // default = true
        return patientRepository.findPatientsByActiveAndUsername(status,username);
    }
    // -------------------- DELETE USER BY ID (ADMIN) --------------------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès");
    }

    // -------------------- DELETE CURRENT USER --------------------
    @DeleteMapping(value = {"/delete-me", "/delete-me/"})
    public ResponseEntity<String> deleteMe(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Utilisateur non authentifié !");
        }
        System.out.println("Suppression du compte connecté : " + userDetails.getUsername());
        userService.deleteMe(userDetails);
        return ResponseEntity.ok("Compte utilisateur désactivé avec succès");
    }

    public Long getPatientIdByUsername(String username) {
        Patient patient = patientRepository.findByUser_Email(username)
                .orElseThrow(() -> new RuntimeException("Docteur non trouvé pour l'utilisateur : " + username));
        return patient.getPatient_id();
    }
}