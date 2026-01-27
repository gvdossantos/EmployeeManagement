package com.projects.employee_management.services;

import com.projects.employee_management.dto.DepartmentResponse;
import com.projects.employee_management.entities.Department;
import com.projects.employee_management.repositories.DepartmentRepository;
import com.projects.employee_management.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {


    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }



    @Transactional(readOnly = true)
    public Page<DepartmentResponse> findAll(Pageable pageable){
        Page<Department> list = repository.findAll(pageable);
        return list.map(DepartmentResponse::new);

    }


    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id){
        Department department = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));

        return new DepartmentResponse(department);
    }

}
