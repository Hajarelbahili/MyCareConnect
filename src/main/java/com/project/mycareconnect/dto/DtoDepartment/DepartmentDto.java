package com.project.mycareconnect.dto.DtoDepartment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mycareconnect.model.Speciality;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DepartmentDto {


    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @Size(max = 500, message = "Description trop longue")
    private String description;


    private String floor;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de téléphone invalide")
    private String phoneNumber;


    private MultipartFile image;


}
