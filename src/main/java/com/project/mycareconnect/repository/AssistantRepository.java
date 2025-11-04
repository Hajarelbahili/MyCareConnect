package com.project.mycareconnect.repository;

import com.project.mycareconnect.model.Assistant;
import com.project.mycareconnect.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssistantRepository extends JpaRepository<Assistant, Long> {

    Optional<Assistant> findByUserId(Long id);
    Assistant findByUser_Email(String email);
    boolean existsByMatriculeAndAssistantIdNot(String matricule, Long assistantId);

    @Query("SELECT a FROM Assistant a " +
            "JOIN FETCH a.user u " +
            "LEFT JOIN FETCH a.department dept " +
            "WHERE ( u.email = :email) ")
    Optional<Assistant> getMe(
            @Param("email") String email
    );

    @Query("SELECT a FROM Assistant a " +
            "JOIN FETCH a.user u " +
            "LEFT JOIN FETCH a.department dept " +
            "WHERE (:department IS NULL OR dept.departmentId = :department) " +
            "AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
            "AND (:active IS NULL OR u.active = :active)")
    List<Assistant> searchAssistants(
            @Param("department") Long department,
            @Param("username") String username,
            @Param("active") Boolean active
    );


}


