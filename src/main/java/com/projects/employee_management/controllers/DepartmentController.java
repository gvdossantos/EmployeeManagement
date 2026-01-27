package com.projects.employee_management.controllers;

import com.projects.employee_management.dto.DepartmentResponse;
import com.projects.employee_management.services.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService service;


    public DepartmentController(DepartmentService service) {
        this.service = service;
    }


    @GetMapping
    @Operation(summary = "Busca todos os departamentos")
    public ResponseEntity<Page<DepartmentResponse>> findAll( @ParameterObject Pageable pageable){
        Page<DepartmentResponse> list = service.findAll(pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca departamentos por id")
    public ResponseEntity<DepartmentResponse> findById(@PathVariable("id") Long id){
        DepartmentResponse reponse = service.findById(id);
        return ResponseEntity.ok(reponse);
    }


}
