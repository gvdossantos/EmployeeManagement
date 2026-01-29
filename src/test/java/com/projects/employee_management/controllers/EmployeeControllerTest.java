

package com.projects.employee_management.controllers;


import com.projects.employee_management.config.TestSecurityConfig;
import com.projects.employee_management.dto.EmployeeResponse;
import com.projects.employee_management.factories.EmployeeFactory;
import com.projects.employee_management.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private JwtEncoder jwtEncoder;


    private Long existingId;


    @BeforeEach
    void setUp() {

        existingId = 1L;
        EmployeeResponse employeeResponse = EmployeeFactory.createEmployeeResponse();

        Mockito.doNothing().when(employeeService).deactivate(existingId);
        Mockito.when(employeeService.findById(existingId)).thenReturn(employeeResponse);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deactivateShouldReturnNothingWhenAccessedByADMIN() throws Exception {


        mockMvc.perform(patch("/employees/" + existingId + "/deactivate"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void deactivateShouldReturnForbiddenWhenAccessedByManager() throws Exception {

        mockMvc.perform(patch("/employees/" + existingId + "/deactivate"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = {"MANAGER", "ADMIN"})
    public void findByIdShouldReturnEmployeeResponseWhenAccessedByManagerOrAdmin() throws Exception {

        ResultActions result = mockMvc.perform(get("/employees/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
    }






}



