package com.project.mycareconnect.dto.DtoDoctor;


import com.project.mycareconnect.enums.Gender;
import com.project.mycareconnect.enums.Role;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DoctorDto {
//Info User
@NotBlank(message = "Le nom d'utilisateur est obligatoire")
private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "Le mot de passe doit contenir au moins une majuscule, une minuscule et un caractère spécial"
    )
    private String password;


    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;


    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de téléphone invalide")
    private String phone;


    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    @NotNull(message = "Le GENRE est obligatoire")
    private Gender gender;
//Info Doctor

    @NotNull(message = "La spécialité est obligatoire")
    private Long specialityId;
    @NotNull(message = "Le département est obligatoire")
    private Long departmentId;

    @NotNull(message = "La durée de consultation est obligatoire")
    @Min(value = 5, message = "La durée minimale est 5 minutes")
    @Max(value = 240, message = "La durée maximale est 240 minutes")
    private Integer consultationDuration;
}
