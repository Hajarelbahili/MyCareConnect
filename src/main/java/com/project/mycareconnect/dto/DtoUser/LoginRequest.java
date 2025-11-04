package com.project.mycareconnect.dto.DtoUser;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;

    // getters & setters
}