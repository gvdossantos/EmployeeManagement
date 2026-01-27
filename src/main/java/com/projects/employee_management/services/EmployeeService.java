package com.projects.employee_management.services;

import com.projects.employee_management.dto.CreateEmployeeRequest;
import com.projects.employee_management.dto.EmployeeResponse;
import com.projects.employee_management.dto.UpdateEmployeeRequest;
import com.projects.employee_management.entities.Department;
import com.projects.employee_management.entities.Employee;
import com.projects.employee_management.repositories.DepartmentRepository;
import com.projects.employee_management.repositories.EmployeeRepository;
import com.projects.employee_management.services.exceptions.BusinessException;
import com.projects.employee_management.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EmployeeService {


    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;


    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));

        return new EmployeeResponse(employee);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> employeesByDepartment(Long depId, Pageable pageable){

        if(!departmentRepository.existsById(depId)){
            throw new ResourceNotFoundException("Id não encontrado: " + depId);
        }

        Page<Employee> list = repository.findByDepartmentId(depId, pageable);
        return list.map(EmployeeResponse::new);

    }


    @Transactional
    public EmployeeResponse insert(CreateEmployeeRequest dto) {

        Employee entity = new Employee();
        entity.setName(dto.name());
        entity.setEmail(dto.email());

        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + dto.departmentId()));


        entity.setDepartment(department);
        entity.setActive(true);

        entity = repository.save(entity);
        return new EmployeeResponse(entity);
    }


    @Transactional
    public EmployeeResponse update(Long id, UpdateEmployeeRequest dto) {

        try {
            Employee entity = repository.getReferenceById(id);
            entity.setName(dto.name());
            entity.setEmail(dto.email());

            Department department = departmentRepository.findById(dto.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + dto.departmentId()));

            entity.setDepartment(department);

            entity = repository.save(entity);
            return new EmployeeResponse(entity);


        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id não encontrado: " + id);
        }
    }


    @Transactional
    public void deactivate(Long id) {
        Employee entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));

        if (!entity.isActive()) {
            throw new BusinessException("Funcionário já está inativo");
        }

        entity.setActive(false);
    }


    @Transactional
    public void reactivate(Long id) {

        Employee entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));

        if (entity.isActive()) {
            throw new BusinessException("Funcionário já está ativo");
        }

        entity.setActive(true);
    }
}
