package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoUser.ForgotPasswordRequest;
import com.project.mycareconnect.dto.DtoUser.LoginRequest;
import com.project.mycareconnect.dto.DtoUser.LoginResponse;
import com.project.mycareconnect.dto.DtoUser.ResetPasswordRequest;
import com.project.mycareconnect.service.AuthService;
import com.project.mycareconnect.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService,PasswordResetService passwordResetService) {
        this.authService = authService;
       this.passwordResetService=passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String jwt = authService.login(request);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.generateResetToken(request.getEmail());
        return ResponseEntity.ok("Email de réinitialisation envoyé");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
    }
}
