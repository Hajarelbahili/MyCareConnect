package com.project.mycareconnect.repository;

import com.project.mycareconnect.enums.Speciality;
import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);

    @Override
    Optional<Department> findById(Long integer);
    boolean existsByNameAndDepartmentIdNot(String name, Long departmentId);
    @Query("SELECT d FROM Department d " +
            "WHERE (:department IS NULL OR d.name = :department) " +
            "AND (:active IS NULL OR d.active = :active)")
    List<Department> searchDepartments(
            @Param("department") String department,
            @Param("active") Boolean active
    );

}
