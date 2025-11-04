package com.project.mycareconnect.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.mycareconnect.enums.Gender;
import com.project.mycareconnect.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    @Column(unique = true, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Lob
    @Column(name = "image")
    private byte[] imageData;
    private String imageName;
    private String imageType;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private boolean active = true;

    // Champs pour reset password
    private String resetPasswordToken;
    private LocalDateTime tokenExpiryDate;
}
