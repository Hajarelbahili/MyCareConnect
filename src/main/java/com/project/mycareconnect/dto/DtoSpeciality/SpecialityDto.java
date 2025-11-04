package com.project.mycareconnect.dto.DtoSpeciality;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.mycareconnect.model.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SpecialityDto {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Size(max = 500, message = "Description trop longue")
    private String description;


}