package com.project.mycareconnect.repository;

import com.project.mycareconnect.enums.Department;
import com.project.mycareconnect.enums.Speciality;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository  extends JpaRepository<Doctor,Long> {

    Optional<Doctor> findByDoctorId(Long id);  // Pour chercher par doctorId
    Optional<Doctor> findByUserId(Long id);
    Optional<Doctor> findByUser(User user);
    @Query("SELECT d FROM Doctor d " +
            "JOIN FETCH d.user u " +
            "LEFT JOIN FETCH d.department dept " +
            "LEFT JOIN FETCH d.speciality s " +
            "WHERE ( u.email = :email) ")
    Optional<Doctor> getMe(
            @Param("email") String email
    );

    @Query("SELECT d FROM Doctor d " +
            "JOIN FETCH d.user u " +
            "LEFT JOIN FETCH d.department dept " +
            "LEFT JOIN FETCH d.speciality s " +
            "WHERE (:department IS NULL OR dept.departmentId = :department) " +
            "AND (:speciality IS NULL OR s.specialityId = :speciality) " +
            "AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
            "AND (:active IS NULL OR u.active = :active)")
    List<Doctor> searchDoctors(
            @Param("department") Long department,
            @Param("speciality") Long speciality,
            @Param("username") String username,
            @Param("active") Boolean active
    );
    Optional<Doctor> findByUser_Email(String email);
}

