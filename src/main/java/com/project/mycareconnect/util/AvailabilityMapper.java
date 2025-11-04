package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoAvailability.AvailabilityDto;
import com.project.mycareconnect.dto.DtoDoctor.DoctorDto;
import com.project.mycareconnect.model.Availability;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class AvailabilityMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // Convertit AvailabilityDto → Availability (DTO -> Entity)
    public Availability fromDto(AvailabilityDto dto) {
        Availability availability = new Availability();
        availability.setDate(LocalDate.parse(dto.getDate(), DATE_FORMATTER));
        availability.setStartTime(LocalTime.parse(dto.getStartTime(), TIME_FORMATTER));
        availability.setEndTime(LocalTime.parse(dto.getEndTime(), TIME_FORMATTER));
        return availability;
    }

    // Convertit Availability → AvailabilityDto (Entity -> DTO)
    public AvailabilityDto toDto(Availability availability) {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setDate(availability.getDate().format(DATE_FORMATTER));
        dto.setStartTime(availability.getStartTime().format(TIME_FORMATTER));
        dto.setEndTime(availability.getEndTime().format(TIME_FORMATTER));
        return dto;
    }
}
