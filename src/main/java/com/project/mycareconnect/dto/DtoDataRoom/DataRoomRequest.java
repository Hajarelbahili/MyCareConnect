package com.project.mycareconnect.dto.DtoDataRoom;

import com.project.mycareconnect.dto.DtoAvailability.AvailabilityDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DataRoomRequest {

    private   Long  uploadedBy ;
    @NotNull
    private   Long  appointmentId;
    @NotEmpty(message = "La liste de documents ne peut pas être vide")
    @Valid
    private List<DataRoomDto> documents;
}