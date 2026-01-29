package com.projects.employee_management.services;

import com.projects.employee_management.dto.DepartmentResponse;
import com.projects.employee_management.entities.Department;
import com.projects.employee_management.factories.DepartmentFactory;
import com.projects.employee_management.repositories.DepartmentRepository;
import com.projects.employee_management.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    private Long existingId;
    private Long nonExistingId;
    private Department department;
    private PageImpl<Department> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 100L;
        department = DepartmentFactory.createDepartment();
        page = new PageImpl<>(List.of(department));

        Mockito.when(departmentRepository.findById(existingId))
                .thenReturn(Optional.of(department));

        Mockito.when(departmentRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        Mockito.when(departmentRepository.findAll((Pageable) ArgumentMatchers.any()))
                .thenReturn(page);

    }

    @Test
    public void findByDepartmentIdShouldReturnDepartmentWhenIdExists() {

       DepartmentResponse result = departmentService.findById(existingId);

        Mockito.verify(departmentRepository).findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId,result.id());
    }


    @Test
    public void findByDepartmentIdShouldThrowResourceNotFoundWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class,
                () ->  departmentService.findById(nonExistingId));

        Mockito.verify(departmentRepository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    public void findAllPageShouldReturnPage(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<DepartmentResponse> result = departmentService.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        Mockito.verify(departmentRepository).findAll(pageable);
    }

}
