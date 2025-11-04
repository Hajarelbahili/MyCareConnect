package com.project.mycareconnect.dto.DtoDoctor;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorResponseDto {
    // Infos User
    private Long userId;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private String phone;
    private String gender;

    // Infos Speciality
    private Long specialityId;
    private String specialityName;

    // Infos Department
    private Long departmentId;
    private String departmentName;
    private Integer consultationDuration;
}

