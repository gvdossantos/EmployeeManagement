package com.projects.employee_management.controllers;

import com.projects.employee_management.dto.DepartmentResponse;
import com.projects.employee_management.factories.DepartmentFactory;
import com.projects.employee_management.services.DepartmentService;
import com.projects.employee_management.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DepartmentController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    private Long existingId;
    private Long nonExistingId;
    private DepartmentResponse  departmentResponse;
    private PageImpl<DepartmentResponse> page;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 100L;
        departmentResponse = DepartmentFactory.createDepartmentResponse();
        page = new PageImpl<>(List.of(departmentResponse), PageRequest.of(0, 10), 1);


        Mockito.when(departmentService.findById(existingId)).thenReturn(departmentResponse);

        Mockito.when(departmentService.findById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Id não encontrado"));

        when(departmentService.findAll(any())).thenReturn(page);
    }


    @Test
    public void findByDepartementIdShouldReturnDepartmentResponseWhenIdExists() throws Exception {

        ResultActions result = mockMvc.perform(get("/departments/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void findByDepartementIdNotFoundWhenIdDoesNotExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/departments/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {

        ResultActions result = mockMvc.perform(get("/departments")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].id").value(existingId));
        result.andExpect(jsonPath("$.content[0].name").value(departmentResponse.name()));
    }


}
