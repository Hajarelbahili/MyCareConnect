package com.project.mycareconnect.dto.DtoUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    // getter
    public String getToken() {
        return token;
    }
}
