package com.project.mycareconnect.dto.DtoAssistant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mycareconnect.enums.Gender;
import com.project.mycareconnect.enums.Role;
import com.project.mycareconnect.model.DataRoom;
import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AssistantDto {

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
//Info Assistant
    @NotNull(message = "Le département est obligatoire")

    private Long departmentId;
    @NotNull(message = "La matricule est obligatoire")
    private String matricule;

    private String fonction;

}


