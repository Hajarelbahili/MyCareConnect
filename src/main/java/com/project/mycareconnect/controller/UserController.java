package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.enums.Role;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserDto userDto) {
        User savedUser = userService.saveUser(userDto);
        return ResponseEntity.status(201).body(savedUser);
    }


    // -------------------- DELETE USER BY ID (ADMIN) --------------------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès");
    }

    // -------------------- DELETE CURRENT USER --------------------
    @DeleteMapping(value = {"/delete-me", "/delete-me/"})
    public ResponseEntity<String> deleteMe(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Utilisateur non authentifié !");
        }
        System.out.println("Suppression du compte connecté : " + userDetails.getUsername());
        userService.deleteMe(userDetails);
        return ResponseEntity.ok("Compte utilisateur désactivé avec succès");
    }

    // -------------------- UPDATE USER BY ID (ADMIN) --------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute UserUpdateDto dto
    ) throws IOException {
        System.out.println("Mise à jour de l'utilisateur ID : " + id);

        User updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    // -------------------- UPDATE CURRENT USER --------------------
    @PutMapping(value = {"/update-me", "/update-me/"})
    public ResponseEntity<User> updateMe(
            @Valid @ModelAttribute UserUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null); // ou un message clair
        }
        System.out.println("Mise à jour du compte connecté : " + userDetails.getUsername());
        User updatedUser = userService.updateCurrentUser(userDetails.getUsername(), dto);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/role")
    public ResponseEntity<List<User>> getUsersByRoleAndStatus(
            @Valid  @RequestParam Role role,
            @RequestParam(defaultValue = "true") boolean active
    ) {
        List<User> users = userService.findByRoleAndActive(role, active);
        return ResponseEntity.ok(users);
    }
}
