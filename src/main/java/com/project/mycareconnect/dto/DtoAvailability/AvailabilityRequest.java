package com.project.mycareconnect.dto.DtoAvailability;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AvailabilityRequest {

    @NotEmpty(message = "La liste des disponibilités ne peut pas être vide")
    @Valid
    private List<AvailabilityDto> availabilities;
}
