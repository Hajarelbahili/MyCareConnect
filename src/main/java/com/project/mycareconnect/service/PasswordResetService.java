package com.project.mycareconnect.service;

import com.project.mycareconnect.exception.TokenExpiredException;
import com.project.mycareconnect.exception.TokenInvalidException;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public void generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        // Envoi par email
        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Réinitialisation de mot de passe");
        mail.setText("Cliquez sur le lien pour réinitialiser votre mot de passe : " + resetLink);

        mailSender.send(mail);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new TokenInvalidException("Token invalide"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Le token a expiré");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);
    }
}