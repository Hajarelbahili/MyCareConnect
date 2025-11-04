package com.project.mycareconnect.repository;

import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    boolean existsByNameAndDepartment(String name, Department department);
    boolean existsByNameAndDepartmentAndSpecialityIdNot(String name, Department department, Long specialityId);

    @Query("SELECT s FROM Speciality s " +
            "WHERE (:speciality IS NULL OR s.name = :speciality) " +
            "AND (:department IS NULL OR s.department.name = :department) " +
            "AND (:active IS NULL OR s.active = :active)")
    List<Speciality> searchSpecialities(
            @Param("speciality") String speciality,
            @Param("department") String department,
            @Param("active") Boolean active
    );

}
