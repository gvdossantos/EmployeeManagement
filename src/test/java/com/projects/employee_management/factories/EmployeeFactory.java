package com.projects.employee_management.factories;

import com.projects.employee_management.dto.EmployeeResponse;
import com.projects.employee_management.entities.Department;
import com.projects.employee_management.entities.Employee;

public class EmployeeFactory {
    
    public static Employee createEmployee() {

        Department department = new Department();
        department.setId(1L);
        department.setName("TI");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setActive(true);
        employee.setName("Teste employee");
        employee.setEmail("employee@gmail.com");
        employee.setDepartment(department);

        return employee;
    }

    public static EmployeeResponse createEmployeeResponse() {
        Employee employee = createEmployee();
        return new EmployeeResponse(employee);
    }
}
