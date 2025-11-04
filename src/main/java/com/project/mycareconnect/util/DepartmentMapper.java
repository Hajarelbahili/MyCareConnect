package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoDepartment.DepartmentDto;
import com.project.mycareconnect.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public Department fromDepartmnetDto(DepartmentDto dto) {
        Department department = new Department();
        department.setEmail(dto.getEmail());
        department.setDescription(dto.getDescription());
        department.setFloor(dto.getFloor());
        department.setName(dto.getName());
        department.setPhoneNumber(dto.getPhoneNumber());
        return department;
    }
    public Department fromDepartmnetDtoupdated(DepartmentDto dto, Department department) {
        department.setEmail(dto.getEmail());
        department.setDescription(dto.getDescription());
        department.setFloor(dto.getFloor());
        department.setName(dto.getName());
        department.setPhoneNumber(dto.getPhoneNumber());
        return department;
    }
}
