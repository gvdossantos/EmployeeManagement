package com.projects.employee_management.controllers;

import com.projects.employee_management.dto.CreateEmployeeRequest;
import com.projects.employee_management.dto.EmployeeResponse;
import com.projects.employee_management.dto.UpdateEmployeeRequest;
import com.projects.employee_management.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;


    public EmployeeController(EmployeeService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Busca funcionário por id")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable("id") Long id){
        EmployeeResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/department/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Busca funcionários pelo id do departamento")
    public ResponseEntity<Page<EmployeeResponse>> employeesByDepartment(
            @PathVariable("id") Long depId, @ParameterObject Pageable pageable){

        Page<EmployeeResponse> list = service.employeesByDepartment(depId, pageable);
        return ResponseEntity.ok(list);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> insert (@RequestBody @Valid CreateEmployeeRequest dto){
        EmployeeResponse result = service.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> update (@PathVariable Long id, @RequestBody @Valid UpdateEmployeeRequest dto){
        EmployeeResponse result = service.update(id, dto);
        return ResponseEntity.ok().body(result);
    }


    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desativa funcionário por id")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ativa funcionário por id")
    public ResponseEntity<Void> reactivate(@PathVariable Long id){
        service.reactivate(id);
        return ResponseEntity.noContent().build();

    }
}
