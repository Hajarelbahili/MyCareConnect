package com.project.mycareconnect.dto.DtoAssistant;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssistantResponseDto {
    // Infos User
    private Long userId;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private String phone;
    private String gender;



    // Infos Department
    private Long departmentId;
    private String departmentName;

    //Assistant
    private  String matricul;
    private String fonction;
}
