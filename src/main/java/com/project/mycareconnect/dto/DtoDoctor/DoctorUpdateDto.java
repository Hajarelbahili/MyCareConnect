package com.project.mycareconnect.dto.DtoDoctor;


import com.project.mycareconnect.enums.Gender;

import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Speciality;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@Getter
@Setter
public class DoctorUpdateDto {
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de téléphone invalide")
    private String phone;
    private Gender gender;
    private MultipartFile image;

    @NotNull(message = "La spécialité est obligatoire")
    private Long specialityId;
    @NotNull(message = "Le département est obligatoire")
    private Long departmentId;
    @Min(value = 5, message = "La durée minimale est 5 minutes")
    @Max(value = 240, message = "La durée maximale est 240 minutes")
    private Integer consultationDuration;

}
