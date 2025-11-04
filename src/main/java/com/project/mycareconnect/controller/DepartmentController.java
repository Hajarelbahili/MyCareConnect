package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoDepartment.DepartmentDto;
import com.project.mycareconnect.enums.Speciality;
import com.project.mycareconnect.model.Department;
import com.project.mycareconnect.model.Doctor;
import com.project.mycareconnect.service.DepartmentService;
import com.project.mycareconnect.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Department> createDepartment(@Valid  @ModelAttribute DepartmentDto dto) {
        Department dept = departmentService.createDepartment(dto);
        return ResponseEntity.status(201).body(dept);
    }
    // ================= UPDATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable("id") Long departmentId,
            @Valid  @ModelAttribute DepartmentDto dto) {

        Department updatedDept = departmentService.updateDepartment(departmentId, dto);
        return ResponseEntity.ok(updatedDept);
    }

    // ================= DELETE =================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("id") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok("Département supprimé avec succès.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/changestatus/{id}/status")
    public ResponseEntity<Department> setDepartmentStatus(
            @PathVariable("id") Long departmentId,
            @RequestParam("active") boolean status) {

        Department updatedDepartment = departmentService.setDepartmentStatus(departmentId, status);
        return ResponseEntity.ok(updatedDepartment);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Department>> searchDepartments(

            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active
    ) {

        List<Department> departments = departmentService.searchDepartments(name,active);
        return departments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(departments);
    }
}

