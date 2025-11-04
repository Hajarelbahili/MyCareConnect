package com.project.mycareconnect.dto.DtoAvailability;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AvailabilityDto {

    @NotBlank(message = "La date est obligatoire")
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "La date doit être au format dd-MM-yyyy")
    private String date;

    @NotBlank(message = "L'heure de début est obligatoire")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "L'heure de début doit être au format HH:mm")
    private String startTime;

    @NotBlank(message = "L'heure de fin est obligatoire")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "L'heure de fin doit être au format HH:mm")
    private String endTime;
}