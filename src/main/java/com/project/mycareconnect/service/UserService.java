package com.project.mycareconnect.service;

import com.project.mycareconnect.dto.DtoUser.UserDto;
import com.project.mycareconnect.dto.DtoUser.UserUpdateDto;
import com.project.mycareconnect.enums.Role;
import com.project.mycareconnect.exception.DuplicateResourceException;
import com.project.mycareconnect.exception.FileProcessingException;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.repository.UserRepository;
import com.project.mycareconnect.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserMapper userMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public User saveUser(UserDto dto) {
        validateUniqueFields(dto);

        User user = userMapper.fromDto(dto);
        return repo.save(user);
    }

    private void validateUniqueFields(UserDto dto) {
        repo.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("Email déjà utilisé");
        });

        if (repo.existsByPhone(dto.getPhone())) {
            throw new DuplicateResourceException("Numéro de téléphone déjà utilisé");
        }
    }


    @Transactional
    public void deleteUser(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable avec ID " + id));
        user.setActive(false);
        repo.save(user);
    }

    @Transactional
    public void deleteMe(UserDetails userDetails) {
        User user = repo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable pour cet email"));
        user.setActive(false);
        repo.save(user);
    }
/*
    public User updateUser(Long id, UserUpdateDto dto) throws IOException {
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + id + " introuvable"));



        // Vérifier téléphone unique
        if (dto.getPhone() != null && !dto.getPhone().equals(user.getPhone())) {
            if (repo.existsByPhone(dto.getPhone())) {
                throw new DuplicateResourceException("Numéro de téléphone déjà utilisé : " + dto.getPhone());
            }
            user.setPhone(dto.getPhone());
        }

        // Mettre à jour les autres champs
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getGender() != null) {
            try {
                user.setGender(dto.getGender());
            } catch (IllegalArgumentException e) {
                throw new InvalidDataException("Valeur du genre invalide : " + dto.getGender());
            }
        }

        // Si une nouvelle image est envoyée
        MultipartFile imageFile = dto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            user.setImageData(imageFile.getBytes());
            user.setImageName(imageFile.getOriginalFilename());
            user.setImageType(imageFile.getContentType());
        }

        return repo.save(user); // updatedAt est modifié automatiquement
    }*/
// ================= UPDATE USER BY ID (ADMIN) =================
public User updateUser(Long id, UserUpdateDto dto) throws IOException {
    User user = repo.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Utilisateur avec ID " + id + " introuvable"));

    updateUserFields(user, dto);

    return repo.save(user);
}

    // ================= UPDATE CURRENT USER (SELF) =================
    public User updateCurrentUser(String email, UserUpdateDto dto) throws IOException {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable avec email : " + email));

        updateUserFields(user, dto);

        return repo.save(user);
    }

    // ================= UTILITY: update fields =================
    private void updateUserFields(User user, UserUpdateDto dto) throws IOException {

        // Vérifier téléphone unique
        if (dto.getPhone() != null && !dto.getPhone().equals(user.getPhone())) {
            if (repo.existsByPhone(dto.getPhone())) {
                throw new DuplicateResourceException("Numéro de téléphone déjà utilisé : " + dto.getPhone());
            }
            user.setPhone(dto.getPhone());
        }

        // Autres champs
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getGender() != null) {
            try {
                user.setGender(dto.getGender());
            } catch (IllegalArgumentException e) {
                throw new InvalidDataException("Valeur du genre invalide : " + dto.getGender());
            }
        }

        // --------- Image ----------
        MultipartFile imageFile = dto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {

            // Valider le type MIME uniquement
            String contentType = imageFile.getContentType();
            if (contentType == null ||
                    !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                throw new InvalidDataException("Format d'image non supporté. Autorisés : JPEG, PNG.");
            }
            System.out.println(dto.getImage());
            // Lecture du fichier
            try {
                user.setImageData(imageFile.getBytes());
                user.setImageName(imageFile.getOriginalFilename());
                user.setImageType(imageFile.getContentType());
            }catch (IOException e) {
                // Fichier corrompu ou lecture impossible
                throw new FileProcessingException("Impossible de lire l'image envoyée.", e);
            }
        }
    }

//LIST DES UTILISATEURS
public List<User> findByRoleAndActive(Role role, boolean active) {
    List<User> users = repo.findByRoleAndActive(role, active);
    if (users.isEmpty()) {
        throw new UserNotFoundException("Aucun utilisateur trouvé avec le rôle " + role + " et active=" + active);
    }
    return users;
}
}