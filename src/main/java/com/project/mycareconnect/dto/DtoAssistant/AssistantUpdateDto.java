package com.project.mycareconnect.dto.DtoAssistant;

import com.project.mycareconnect.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class AssistantUpdateDto {
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de téléphone invalide")
    private String phone;
    private Gender gender;
    private MultipartFile image;
    @NotNull(message = "Le département est obligatoire")
    private Long departmentId;
    //Info Assistant
    @NotNull(message = "Le département est obligatoire")

    private String matricule;

    private String fonction;

}
