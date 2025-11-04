package com.project.mycareconnect.dto.DtoUser;

import com.project.mycareconnect.enums.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@Getter
@Setter
public class UserUpdateDto {
    private String username;
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de téléphone invalide")
    private String phone;
    private Gender gender;
    private MultipartFile image;
}
