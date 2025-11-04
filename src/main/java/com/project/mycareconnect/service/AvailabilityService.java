package com.project.mycareconnect.service;

import com.project.mycareconnect.dto.DtoAvailability.AvailabilityDto;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.exception.UserNotFoundException;
import com.project.mycareconnect.model.Availability;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.User;
import com.project.mycareconnect.repository.AvailabilityRepository;
import com.project.mycareconnect.repository.DoctorRepository;
import com.project.mycareconnect.repository.UserRepository;
import com.project.mycareconnect.util.AvailabilityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final DoctorRepository doctorRepository;
    private final AvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;
    private final AvailabilityMapper availabilityMapper;

    public List<Availability> addAvailabilities(UserDetails userDetails, List<AvailabilityDto> dtoList) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable avec l'email : " + userDetails.getUsername()));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Aucun docteur trouvé pour l'utilisateur id=" + user.getId()));


        List<Availability> availabilities = dtoList.stream().map(dto -> {
            Availability availability = availabilityMapper.fromDto(dto);
            availability.setDoctor(doctor);

            // Vérification des règles métier
            if (availability.getDate().isBefore(LocalDate.now())) {
                throw new InvalidDataException("La date " + dto.getDate() + " est déjà passée.");
            }

            if (!availability.getStartTime().isBefore(availability.getEndTime())) {
                throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
            }

            return availability;
        }).toList();
        try {
            // Ici on sauvegarde tout, la contrainte unique empêche les doublons
            return availabilityRepository.saveAll(availabilities);
        } catch (DataIntegrityViolationException e) {
            // On attrape l'exception de la base et on renvoie un message clair
            throw new InvalidDataException("Une disponibilité existe déjà pour un ou plusieurs créneaux.");
        }
    }

    public List<Availability> getDoctorAvailabilities(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable avec l'email : " + userDetails.getUsername()));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Aucun docteur trouvé pour l'utilisateur id=" + user.getId()));

        return availabilityRepository.findByDoctor(doctor);
    }
}
