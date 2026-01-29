package com.projects.employee_management.factories;

import com.projects.employee_management.dto.DepartmentResponse;
import com.projects.employee_management.entities.Department;

public class DepartmentFactory {

    public static Department createDepartment() {

        Department department = new Department();
        department.setId(1L);
        department.setName("TI");

        return department;
    }

    public static DepartmentResponse createDepartmentResponse () {
        Department department = createDepartment();
        return new DepartmentResponse(department);
    }

}
