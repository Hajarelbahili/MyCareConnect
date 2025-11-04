package com.project.mycareconnect.repository;

import com.project.mycareconnect.enums.Role;
import com.project.mycareconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findByResetPasswordToken(String token);
    // Trouver les utilisateurs par rôle et état actif
    List<User> findByRoleAndActive(Role role, boolean active);
}

