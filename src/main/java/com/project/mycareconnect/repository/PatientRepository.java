package com.project.mycareconnect.repository;

import com.project.mycareconnect.enums.Department;
import com.project.mycareconnect.enums.Speciality;
import com.project.mycareconnect.model.Assistant;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser_Id(Long id);

    @Query("SELECT p FROM Patient p " +
            "WHERE p.user.active = :active " +
            "AND (:username IS NULL OR LOWER(p.user.username) LIKE LOWER(CONCAT('%', :username, '%')))")
    List<Patient> findPatientsByActiveAndUsername(@Param("active") boolean active,
                                                  @Param("username") String username);
Optional<Patient> findByUser_Email(String email);

}
