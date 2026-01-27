package com.projects.employee_management.dto;

import com.projects.employee_management.entities.Employee;

public record EmployeeResponse(
        Long id,
        String name,
        String departmentName,
        boolean active
) {

    public EmployeeResponse (Employee employee) {
        this(
                employee.getId(),
                employee.getName(),
                employee.getDepartment().getName(),
                employee.isActive()
        );
    }
}
