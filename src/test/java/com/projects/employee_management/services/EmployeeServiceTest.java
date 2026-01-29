package com.projects.employee_management.services;

import com.projects.employee_management.entities.Employee;
import com.projects.employee_management.factories.EmployeeFactory;
import com.projects.employee_management.repositories.EmployeeRepository;
import com.projects.employee_management.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {


    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Long existingId;
    private Long nonExistingId;
    private Employee employee;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 1000L;
        employee = EmployeeFactory.createEmployee();

        Mockito.when(employeeRepository.findById(existingId))
                .thenReturn(Optional.of(employee));

        Mockito.when(employeeRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());
    }


    @Test
    public void deactivateShouldDeactivateEmployeeWhenIdExists() {

        employeeService.deactivate(existingId);

        Employee e = employeeRepository.findById(existingId).orElseThrow();

        Assertions.assertFalse(e.isActive());

    }


    @Test
    public void deactivateShouldThrowResourceNotFoundWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deactivate(nonExistingId);
        });

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(nonExistingId);
    }


    @Test
    public void reactivateShouldReactivateEmployeeWhenIdExists() {

        employeeService.deactivate(existingId);
        employeeService.reactivate(existingId);

        Employee e = employeeRepository.findById(existingId).orElseThrow();

        Assertions.assertTrue(e.isActive());

    }


    @Test
    public void activateShoulNotReactivateEmployeeWhenIdDoesNotExits() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.reactivate(nonExistingId);
        });

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(nonExistingId);
    }


}
