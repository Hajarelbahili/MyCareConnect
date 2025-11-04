package com.project.mycareconnect.dto.DtoPatient;

import com.project.mycareconnect.enums.BloodType;
import com.project.mycareconnect.enums.Gender;
import com.project.mycareconnect.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class PatientUpdateDto {


    private String username;


    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;


    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de téléphone invalide")
    private String phone;



    private Gender gender;
    //Info pATIENT
    private BloodType bloodType;
    private MultipartFile image;


}
