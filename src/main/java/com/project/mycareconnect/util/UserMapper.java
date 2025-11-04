package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public User fromDto(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 🔐 hasher le mot de passe
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setGender(dto.getGender());
        return user;
    }

    // Si tu veux faire l'inverse aussi :
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        // pas de mot de passe en retour ⚠️
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setGender(user.getGender());
        return dto;
    }
}
