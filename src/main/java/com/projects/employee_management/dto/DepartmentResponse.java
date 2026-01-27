package com.projects.employee_management.dto;

import com.projects.employee_management.entities.Department;

public record DepartmentResponse(
        Long id,
        String name
) {
    public DepartmentResponse(Department department) {
        this(
                department.getId(),
                department.getName()
        );
    }
}
