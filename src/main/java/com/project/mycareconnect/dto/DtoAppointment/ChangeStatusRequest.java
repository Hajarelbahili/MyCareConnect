package com.project.mycareconnect.dto.DtoAppointment;

import com.project.mycareconnect.enums.AppointmentStatus;
import lombok.Data;

@Data
public class ChangeStatusRequest {
    private AppointmentStatus status;
    private Long appointmentId;
}
